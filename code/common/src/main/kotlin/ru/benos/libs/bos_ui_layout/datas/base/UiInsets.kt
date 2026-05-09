package ru.benos.libs.bos_ui_layout.datas.base

data class UiInsets(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
) {
    constructor(horizontal: Int, vertical: Int) :
            this(horizontal, vertical, horizontal, vertical)

    constructor(all: Int) :
            this(all, all, all, all)

    companion object {
        val ZERO: UiInsets = UiInsets(0, 0, 0, 0)
    }

    val horizontal: Int
        get() = left + right

    val vertical: Int
        get() = top + bottom
}