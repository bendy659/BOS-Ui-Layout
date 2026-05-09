package ru.benos.libs.bos_ui_layout.datas

import ru.benos.libs.bos_ui_layout.datas.base.UiSize
import ru.benos.libs.bos_ui_layout.enum.UiAlign

data class UiAligns(
    val horizontal: UiAlign,
    val vertical: UiAlign
) {
    companion object {
        val LEFT_TOP_CORNER: UiAligns
            get() = UiAligns(UiAlign.Start, UiAlign.Start)

        val TOP_CENTER: UiAligns
            get() = UiAligns(UiAlign.Center, UiAlign.Start)

        val RIGHT_TOP_CORNER: UiAligns
            get() = UiAligns(UiAlign.End, UiAlign.Start)

        val LEFT_CENTER: UiAligns
            get() = UiAligns(UiAlign.Start, UiAlign.Center)

        val CENTER: UiAligns
            get() = UiAligns(UiAlign.Center, UiAlign.Center)

        val RIGHT_CENTER: UiAligns
            get() = UiAligns(UiAlign.End, UiAlign.Center)

        val LEFT_BOTTOM_CORNER: UiAligns
            get() = UiAligns(UiAlign.Start, UiAlign.End)

        val BOTTOM_CENTER: UiAligns
            get() = UiAligns(UiAlign.Center, UiAlign.End)

        val RIGHT_BOTTOM_CORNER: UiAligns
            get() = UiAligns(UiAlign.End, UiAlign.End)
    }

    fun calcAligns(inner: UiSize, size: UiSize): Pair<Int, Int> {
        val horizontal = horizontal.calcAlign(inner.width, size.width)
        val vertical = vertical.calcAlign(inner.height, size.height)

        return horizontal to vertical
    }
}