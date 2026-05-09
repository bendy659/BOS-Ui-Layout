package ru.benos.libs.bos_ui_layout.datas.base

import kotlin.math.max

data class UiSize(
    val width: Int,
    val height: Int
) {
    companion object {
        val ZERO: UiSize = UiSize(0, 0)
    }

    fun maxOf(vWidth: Int, vHeight: Int): UiSize = UiSize(
        width = max(width, vWidth),
        height = max(height, vHeight)
    )

    fun maxOf(other: UiSize): UiSize =
        maxOf(other.width, other.height)
}