package ru.benos.libs.bos_ui_layout.enum

import ru.benos.libs.bos_ui_layout.datas.base.UiRect

enum class UiTextAlign {
    Left, Center, Right;

    fun calcOffsetX(inner: UiRect, lineWidth: Int): Int =
        when (this) {
            Left -> inner.x
            Center -> inner.x + ((inner.width - lineWidth) / 2).coerceAtLeast(0)
            Right -> inner.right - lineWidth
        }
}