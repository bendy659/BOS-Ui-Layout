package ru.benos.libs.ui_layout.data

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation

actual fun renderFillRect(guiGraphics: GuiGraphics, bound: UiRect, color: UiColor) =
    guiGraphics.fill(bound.x, bound.y, bound.right, bound.bottom, color.int)

actual fun renderOutlineColor(guiGraphics: GuiGraphics, bound: UiRect, color: UiColor, width: Int) {
    guiGraphics.fill(bound.x, bound.y, bound.right, bound.y + width, color.int) // top
    guiGraphics.fill(bound.x, bound.bottom - width, bound.right, bound.bottom, color.int) // bottom
    guiGraphics.fill(bound.x, bound.y + width, bound.x + width, bound.bottom - width, color.int) // left
    guiGraphics.fill(bound.right - width, bound.y + width, bound.right, bound.bottom - width, color.int) // right
}

actual fun renderFlatTexture(guiGraphics: GuiGraphics, bound: UiRect, color: UiColor, textureId: String, textureUv: UiRect) {
    guiGraphics.setColor(color.rFloat, color.gFloat, color.bFloat, color.aFloat)
    guiGraphics.blit(
        ResourceLocation.parse(textureId),
        bound.x, bound.y,
        textureUv.x.toFloat(), textureUv.y.toFloat(),
        textureUv.width, textureUv.height,
        bound.width, bound.height
    )
    guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f)
}

actual fun renderNineSliceTexture(guiGraphics: GuiGraphics, bound: UiRect, color: UiColor, resource: String) {
    guiGraphics.setColor(color.rFloat, color.gFloat, color.bFloat, color.aFloat)
    guiGraphics.blitSprite(
        ResourceLocation.parse(resource),
        bound.x, bound.y,
        bound.width, bound.height
    )
    guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f)
}