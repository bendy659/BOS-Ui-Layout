package ru.benos.libs.bos_ui_layout.client.datas

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation

sealed interface IUiCanvas {
    val color: UiColor

    fun render(guiGraphics: GuiGraphics, bounds: UiRect)

    class Fill(
        override val color: UiColor
    ) : IUiCanvas {
        override fun render(guiGraphics: GuiGraphics, bounds: UiRect) =
            guiGraphics.fill(bounds.x, bounds.y, bounds.right, bounds.bottom, color.int)
    }

    class Gradient(
        override val color: UiColor,
        val color2: UiColor
    ) : IUiCanvas {
        override fun render(guiGraphics: GuiGraphics, bounds: UiRect) =
            guiGraphics.fillGradient(bounds.x, bounds.y, bounds.right, bounds.bottom, color.int, color2.int)
    }

    class Outline(
        override val color: UiColor,
        val width: Int
    ) : IUiCanvas {
        override fun render(guiGraphics: GuiGraphics, bounds: UiRect) {
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
        }
    }

    class Texture(
        override val color: UiColor,
        val resource: String,
        val textureUv: UiRect
    ) : IUiCanvas {
        override fun render(guiGraphics: GuiGraphics, bounds: UiRect) {
            guiGraphics.setColor(color.rFloat, color.gFloat, color.bFloat, color.aFloat)
            guiGraphics.blit(
                ResourceLocation.parse(resource),
                bounds.x, bounds.y,
                textureUv.x.toFloat(), textureUv.y.toFloat(),
                textureUv.width, textureUv.height,
                bounds.width, bounds.height
            )
            guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f)
        }
    }

    class NineSliceTexture(
        override var color: UiColor,
        val resource: String
    ) : IUiCanvas {
        override fun render(guiGraphics: GuiGraphics, bounds: UiRect) {
            guiGraphics.setColor(color.rFloat, color.gFloat, color.bFloat, color.aFloat)
            guiGraphics.blitSprite(
                ResourceLocation.parse(resource),
                bounds.x, bounds.y,
                bounds.width, bounds.height
            )
            guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f)
        }
    }

    class Shader(
        override val color: UiColor,
        val resource: String
    ) : IUiCanvas {
        override fun render(guiGraphics: GuiGraphics, bounds: UiRect) {
            TODO()
        }
    }
}