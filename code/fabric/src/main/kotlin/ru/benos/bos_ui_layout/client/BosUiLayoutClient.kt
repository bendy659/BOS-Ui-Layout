package ru.benos.bos_ui_layout.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.ResourceManager
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL30
import ru.benos.libs.bos_ui_layout.UiShaderCache
import ru.benos.libs.bos_ui_layout.demos.DemoUiHud0
import ru.benos.libs.bos_ui_layout.demos.DemoUiScreen0

object BosUiLayoutClient: ClientModInitializer {
    const val MOD_ID: String = "bos_ui_layout"

    private var isSteciled: Boolean = false

    override fun onInitializeClient() {
        System.loadLibrary("renderdoc")

        val key = KeyBindingHelper.registerKeyBinding(KeyMapping("a", GLFW.GLFW_KEY_UNKNOWN, "c"))

        ClientTickEvents.END_CLIENT_TICK.register { _ ->
            if (!isSteciled) {
                enableStencil()
                initStencil()
                isSteciled = true
            }

            if (key.consumeClick())
                Minecraft.getInstance().setScreen(DemoUiScreen0())
        }

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(object :
            SimpleSynchronousResourceReloadListener {
            override fun getFabricId() = ResourceLocation.parse("bos_ui_layout:shader_cache")

            override fun onResourceManagerReload(manager: ResourceManager) {
                UiShaderCache.clear()
            }
        })

        HudRenderCallback.EVENT.register { graphics, _ -> DemoUiHud0.render(graphics) }
    }

    fun enableStencil() {
        val mainTarget = Minecraft.getInstance().mainRenderTarget

        // Биндим framebuffer
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, mainTarget.frameBufferId)

        // Создаём stencil renderbuffer
        val stencilBuffer = GL30.glGenRenderbuffers()
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, stencilBuffer)
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH24_STENCIL8, mainTarget.width, mainTarget.height)

        // Прикрепляем к framebuffer
        GL30.glFramebufferRenderbuffer(
            GL30.GL_FRAMEBUFFER,
            GL30.GL_DEPTH_STENCIL_ATTACHMENT,
            GL30.GL_RENDERBUFFER,
            stencilBuffer
        )

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0)
    }

    fun initStencil() {
        val mainTarget = Minecraft.getInstance().mainRenderTarget
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, mainTarget.frameBufferId)

        val depthStencilBuffer = GL30.glGenRenderbuffers()
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthStencilBuffer)
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH24_STENCIL8, mainTarget.width, mainTarget.height)
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthStencilBuffer)
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_STENCIL_ATTACHMENT, GL30.GL_RENDERBUFFER, depthStencilBuffer)

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0)
    }
}