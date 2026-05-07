package ru.benos.libs.bos_ui_layout.client.datas

data class UiSize(
    val width: Int,
    val height: Int
) {
    companion object {
        val ZERO: UiSize = UiSize(0, 0)
    }
}