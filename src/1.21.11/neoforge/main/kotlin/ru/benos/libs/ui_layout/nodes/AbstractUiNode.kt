package ru.benos.libs.ui_layout.nodes

import net.minecraft.client.gui.GuiGraphics
import ru.benos.libs.ui_layout.data.UiModifier
import ru.benos.libs.ui_layout.data.UiRect

actual fun renderTransformedNode(guiGraphics: GuiGraphics, bounds: UiRect, modifier: UiModifier, block: () -> Unit) {
    guiGraphics.pose().pushMatrix()

    val pivotX = bounds.x + bounds.width * modifier.transform.origin.x
    val pivotY = bounds.y + bounds.height * modifier.transform.origin.y

    val offset = modifier.transform.offset
    val rotation = modifier.transform.rotation
    val scale = modifier.transform.scale

    guiGraphics.pose().translate(offset.x, offset.y)
    guiGraphics.pose().rotateAbout(rotation, pivotX, pivotY)
    guiGraphics.pose().scaleAround(scale.x, scale.y, pivotX, pivotY)

    block()

    guiGraphics.pose().popMatrix()
}