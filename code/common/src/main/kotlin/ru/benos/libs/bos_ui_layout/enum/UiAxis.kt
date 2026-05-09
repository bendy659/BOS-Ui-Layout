package ru.benos.libs.bos_ui_layout.enum

import ru.benos.libs.bos_ui_layout.datas.base.UiSize

enum class UiAxis {
    Horizontal, Vertical;

    fun calcContentSize(measured: List<UiSize>, gap: Int): UiSize {
        val totalGap = (gap * (measured.size - 1)).coerceAtLeast(0)
        return when (this) {
            Horizontal -> UiSize(
                measured.sumOf(UiSize::width) + totalGap,
                measured.maxOfOrNull(UiSize::height) ?: 0
            )

            Vertical -> UiSize(
                measured.maxOfOrNull(UiSize::width) ?: 0,
                measured.sumOf(UiSize::height) + totalGap
            )
        }
    }
}