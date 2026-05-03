package ru.benos.libs.ui_layout.data

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier

actual fun renderFillRect(guiGraphics: GuiGraphics, bound: UiRect, color: UiColor) =
    guiGraphics.fill(bound.x, bound.y, bound.right, bound.bottom, color.int)

actual fun renderOutlineColor(guiGraphics: GuiGraphics, bound: UiRect, color: UiColor, width: Int) {
    guiGraphics.fill(bound.x, bound.y, bound.right, bound.y + width, color.int) // top
    guiGraphics.fill(bound.x, bound.bottom - width, bound.right, bound.bottom, color.int) // bottom
    guiGraphics.fill(bound.x, bound.y + width, bound.x + width, bound.bottom - width, color.int) // left
    guiGraphics.fill(bound.right - width, bound.y + width, bound.right, bound.bottom - width, color.int) // right
}

actual fun renderFlatTexture(guiGraphics: GuiGraphics, bound: UiRect, color: UiColor, textureId: String, textureUv: UiRect) {
    guiGraphics.blit(
        RenderPipelines.GUI_TEXTURED,
        Identifier.parse(textureId),
        bound.x, bound.y,
        textureUv.x.toFloat(), textureUv.y.toFloat(),
        textureUv.width, textureUv.height,
        bound.width, bound.height,
        color.int
    )
}

actual fun renderNineSliceTexture(guiGraphics: GuiGraphics, bound: UiRect, color: UiColor, resource: String) {
    guiGraphics.blitSprite(
        RenderPipelines.GUI_TEXTURED,
        Identifier.parse(resource),
        bound.x, bound.y,
        bound.width, bound.height,
        color.int
    )
}