package ru.benos.libs.ui_layout.nodes

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.util.FormattedCharSequence

actual fun renderDrawString(guiGraphics: GuiGraphics, font: Font, line: FormattedCharSequence, drawX: Int, drawY: Int, enableLabelShadow: Boolean) {
    guiGraphics.drawString(font, line, drawX, drawY, 0x00000000, enableLabelShadow)
}