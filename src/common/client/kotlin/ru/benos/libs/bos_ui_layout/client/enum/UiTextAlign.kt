package ru.benos.libs.bos_ui_layout.client.enum

import ru.benos.libs.bos_ui_layout.client.datas.UiRect

enum class UiTextAlign {
    Left, Center, Right;

    fun calcOffsetX(inner: UiRect, lineWidth: Int): Int =
        when (this) {
            Left   -> inner.x
            Center -> ((inner.width - lineWidth) / 2).coerceAtLeast(0)
            Right  -> inner.right - lineWidth
        }
}