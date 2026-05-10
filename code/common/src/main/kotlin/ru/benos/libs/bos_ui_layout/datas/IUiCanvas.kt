package ru.benos.libs.bos_ui_layout.datas

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import org.joml.Matrix4f
import org.joml.Vector4f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import ru.benos.libs.bos_ui_layout.UiShaderCache
import ru.benos.libs.bos_ui_layout.datas.base.UiColor
import ru.benos.libs.bos_ui_layout.datas.base.UiNodeContext
import ru.benos.libs.bos_ui_layout.datas.base.UiRect

sealed interface IUiCanvas {
    val color: UiColor

    fun render(guiGraphics: GuiGraphics, bounds: UiRect, ctx: UiNodeContext, block: () -> Unit)

    class Fill(
        override val color: UiColor
    ) : IUiCanvas {
        override fun render(guiGraphics: GuiGraphics, bounds: UiRect, ctx: UiNodeContext, block: () -> Unit) {
            guiGraphics.fill(bounds.x, bounds.y, bounds.right, bounds.bottom, color.int)

            block()
        }
    }

    class Gradient(
        override val color: UiColor,
        val color2: UiColor
    ) : IUiCanvas {
        override fun render(guiGraphics: GuiGraphics, bounds: UiRect, ctx: UiNodeContext, block: () -> Unit) {
            guiGraphics.fillGradient(bounds.x, bounds.y, bounds.right, bounds.bottom, color.int, color2.int)

            block()
        }
    }

    class Outline(
        override val color: UiColor,
        val width: Int
    ) : IUiCanvas {
        override fun render(guiGraphics: GuiGraphics, bounds: UiRect, ctx: UiNodeContext, block: () -> Unit) {
            guiGraphics.fill(bounds.x, bounds.y, bounds.right, bounds.y + width, color.int) // top
            guiGraphics.fill(bounds.x, bounds.bottom - width, bounds.right, bounds.bottom, color.int) // bottom
            guiGraphics.fill(bounds.x, bounds.y + width, bounds.x + width, bounds.bottom - width, color.int) // left
            guiGraphics.fill(
                bounds.right - width,
                bounds.y + width,
                bounds.right,
                bounds.bottom - width,
                color.int
            ) // right

            block()
        }
    }

    class Texture(
        override val color: UiColor,
        val resource: String,
        val textureUv: UiRect
    ) : IUiCanvas {
        override fun render(guiGraphics: GuiGraphics, bounds: UiRect, ctx: UiNodeContext, block: () -> Unit) {
            guiGraphics.setColor(color.rFloat, color.gFloat, color.bFloat, color.aFloat)
            guiGraphics.blit(
                ResourceLocation.parse(resource),
                bounds.x, bounds.y,
                textureUv.x.toFloat(), textureUv.y.toFloat(),
                textureUv.width, textureUv.height,
                bounds.width, bounds.height
            )
            guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f)

            block()
        }
    }

    class NineSliceTexture(
        override var color: UiColor,
        val resource: String
    ) : IUiCanvas {
        override fun render(guiGraphics: GuiGraphics, bounds: UiRect, ctx: UiNodeContext, block: () -> Unit) {
            guiGraphics.setColor(color.rFloat, color.gFloat, color.bFloat, color.aFloat)
            guiGraphics.blitSprite(
                ResourceLocation.parse(resource),
                bounds.x, bounds.y,
                bounds.width, bounds.height
            )
            guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f)

            block()
        }
    }

    class Shader(
        override val color: UiColor,
        val resource: String,
        val setup: UiShaderCache.Builder.(UiNodeContext) -> Unit = { }
    ) : IUiCanvas {
        override fun render(guiGraphics: GuiGraphics, bounds: UiRect, ctx: UiNodeContext, block: () -> Unit) {
            val program = UiShaderCache.get(resource)
            val mc = Minecraft.getInstance()

            guiGraphics.flush()

            val mainTarget = mc.mainRenderTarget
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, mainTarget.frameBufferId)

            // 1. Маска в stencil
            GL11.glEnable(GL11.GL_STENCIL_TEST)
            GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT)
            GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE)
            GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF)
            GL11.glStencilMask(0xFF)
            GL11.glColorMask(false, false, false, false)
            GL11.glDepthMask(false)

            // Маска по трансформированным вершинам
            val matrix = guiGraphics.pose().last().pose()
            val v00 = Vector4f(bounds.x.toFloat(), bounds.y.toFloat(), 0f, 1f).mul(matrix)
            val v01 = Vector4f(bounds.x.toFloat(), bounds.bottom.toFloat(), 0f, 1f).mul(matrix)
            val v11 = Vector4f(bounds.right.toFloat(), bounds.bottom.toFloat(), 0f, 1f).mul(matrix)
            val v10 = Vector4f(bounds.right.toFloat(), bounds.y.toFloat(), 0f, 1f).mul(matrix)

            // Конвертируем в screen coords
            val proj = RenderSystem.getProjectionMatrix()
            val mv = RenderSystem.getModelViewMatrix()

            drawRawQuadWithMatrices(floatArrayOf(
                v00.x, v00.y, 0f,
                v01.x, v01.y, 0f,
                v11.x, v11.y, 0f,
                v10.x, v10.y, 0f
            ), mv, proj)

            // 2. Рендерим шейдер по маске
            GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF)
            GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP)
            GL11.glStencilMask(0x00)
            GL11.glColorMask(true, true, true, true)
            GL11.glDepthMask(true)

            GL20.glUseProgram(program)

            val buf16 = org.lwjgl.BufferUtils.createFloatBuffer(16)
            val modelViewLoc = GL20.glGetUniformLocation(program, "ModelViewMat")
            val projLoc = GL20.glGetUniformLocation(program, "ProjMat")

            mv.get(buf16); buf16.rewind()
            GL20.glUniformMatrix4fv(modelViewLoc, false, buf16)

            proj.get(buf16); buf16.rewind()
            GL20.glUniformMatrix4fv(projLoc, false, buf16)

            val colorLoc = GL20.glGetUniformLocation(program, "Color")
            GL20.glUniform4f(colorLoc, color.rFloat, color.gFloat, color.bFloat, color.aFloat)

            UiShaderCache.Builder(program).setup(ctx)

            val screenW = mc.window.guiScaledWidth.toFloat()
            val screenGH = mc.window.guiScaledHeight.toFloat()

            drawRawQuadWithMatrices(floatArrayOf(
                0f, 0f, 0f,
                0f, screenGH, 0f,
                screenW, screenGH, 0f,
                screenW, 0f, 0f
            ), mv, proj)

            // 3. Выключаем
            GL11.glDisable(GL11.GL_STENCIL_TEST)
            GL11.glStencilMask(0xFF)
            GL20.glUseProgram(0)

            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0)

            block()
        }

        private fun drawRawQuadWithMatrices(vertices: FloatArray, mv: Matrix4f, proj: Matrix4f) {
            val vao = GL30.glGenVertexArrays()
            val vbo = GL15.glGenBuffers()

            GL30.glBindVertexArray(vao)
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)

            val vertBuf = org.lwjgl.BufferUtils.createFloatBuffer(vertices.size)
            vertBuf.put(vertices).rewind()
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertBuf, GL15.GL_DYNAMIC_DRAW)

            GL20.glEnableVertexAttribArray(0)
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0)

            // Временная программа для маски
            val maskProgram = UiShaderCache.getSimple()
            GL20.glUseProgram(maskProgram)

            val buf16 = org.lwjgl.BufferUtils.createFloatBuffer(16)
            val mvLoc = GL20.glGetUniformLocation(maskProgram, "ModelViewMat")
            val pLoc = GL20.glGetUniformLocation(maskProgram, "ProjMat")
            mv.get(buf16); buf16.rewind(); GL20.glUniformMatrix4fv(mvLoc, false, buf16)
            proj.get(buf16); buf16.rewind(); GL20.glUniformMatrix4fv(pLoc, false, buf16)

            GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, 4)

            GL20.glDisableVertexAttribArray(0)
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
            GL30.glBindVertexArray(0)
            GL30.glDeleteVertexArrays(vao)
            GL15.glDeleteBuffers(vbo)
        }
    }
}