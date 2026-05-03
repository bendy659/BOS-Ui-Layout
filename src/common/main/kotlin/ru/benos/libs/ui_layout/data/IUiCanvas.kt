package ru.benos.libs.ui_layout.data

import net.minecraft.client.gui.GuiGraphics

sealed interface IUiCanvas {
    val color: UiColor

    fun render(guiGraphics: GuiGraphics, bound: UiRect)

    class FillRect(override var color: UiColor): IUiCanvas {
        constructor(r: Int, g: Int, b: Int, a: Int = 255):
                this(UiColor(r, g, b, a))

        override fun render(guiGraphics: GuiGraphics, bound: UiRect) =
            renderFillRect(guiGraphics, bound, color)
    }

    class OutlineColor(override var color: UiColor, var width: Int): IUiCanvas {
        constructor(r: Int, g: Int, b: Int, a: Int = 255, width: Int):
                this(UiColor(r, g, b, a), width)

        override fun render(guiGraphics: GuiGraphics, bound: UiRect) =
            renderOutlineColor(guiGraphics, bound, color, width)
    }

    class FlatTexture(override var color: UiColor, val textureId: String, val textureUv: UiRect): IUiCanvas {
        override fun render(guiGraphics: GuiGraphics, bound: UiRect) =
            renderFlatTexture(guiGraphics, bound, color, textureId, textureUv)
    }

    class NineSliceTexture(override var color: UiColor, val textureId: String): IUiCanvas {
        override fun render(guiGraphics: GuiGraphics, bound: UiRect) =
            renderNineSliceTexture(guiGraphics, bound, color, textureId)
    }
}

expect fun renderFillRect(guiGraphics: GuiGraphics, bound: UiRect, color: UiColor)

expect fun renderOutlineColor(guiGraphics: GuiGraphics, bound: UiRect, color: UiColor, width: Int)

expect fun renderFlatTexture(guiGraphics: GuiGraphics, bound: UiRect, color: UiColor, textureId: String, textureUv: UiRect)

expect fun renderNineSliceTexture(guiGraphics: GuiGraphics, bound: UiRect, color: UiColor, resource: String)