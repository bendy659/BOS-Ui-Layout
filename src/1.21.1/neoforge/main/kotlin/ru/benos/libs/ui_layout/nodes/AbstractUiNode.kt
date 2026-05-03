package ru.benos.libs.ui_layout.nodes

import com.mojang.math.Axis
import net.minecraft.client.gui.GuiGraphics
import ru.benos.libs.ui_layout.data.UiModifier
import ru.benos.libs.ui_layout.data.UiRect

actual fun renderTransformedNode(guiGraphics: GuiGraphics, bounds: UiRect, modifier: UiModifier, block: () -> Unit) {
    guiGraphics.pose().pushPose()

    val pivotX = bounds.x + bounds.width * modifier.transform.origin.x
    val pivotY = bounds.y + bounds.height * modifier.transform.origin.y

    val offset = modifier.transform.offset
    val rotation = modifier.transform.rotation
    val scale = modifier.transform.scale

    guiGraphics.pose().translate(offset.x, offset.y, 0.0f)
    guiGraphics.pose().rotateAround(Axis.ZP.rotation(rotation), pivotX, pivotY, 0.0f)

    // Scale around //
    guiGraphics.pose().translate(pivotX, pivotY, 0.0f)
    guiGraphics.pose().scale(scale.x, scale.y, 1.0f)
    guiGraphics.pose().translate(-pivotX, -pivotY, 0.0f)

    block()

    guiGraphics.pose().popPose()
}