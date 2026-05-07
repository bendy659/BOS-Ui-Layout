package ru.benos.libs.bos_ui_layout.client.enum

enum class UiAlign {
    Start, Center, End;

    fun calcAlign(inner: Int, size: Int): Int =
        when (this) {
            Start  -> 0
            Center -> (inner - size) / 2
            End    -> inner - size
        }
}