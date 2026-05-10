package ru.benos.libs.bos_ui_layout.datas.base

data class UiRect(
    val x: Int, val y: Int,
    val width: Int, val height: Int
) {
    constructor(x: Int, y: Int, size: UiSize) :
            this(x, y, size.width, size.height)

    constructor(rect: UiRect, size: UiSize) :
            this(rect.x, rect.y, size.width, size.height)

    companion object {
        val ZERO: UiRect
            get() = UiRect(0, 0, 0, 0)

        val X16: UiRect
            get() = UiRect(0, 0, 16, 16)

        fun stretch(width: Int, height: Int): UiRect =
            UiRect(0, 0, width, height)

        fun stretch(size: UiSize): UiRect =
            stretch(size.width, size.height)
    }

    val right: Int
        get() = x + width

    val bottom: Int
        get() = y + height

    val size: UiSize
        get() = UiSize(width, height)

    fun shrink(insents: UiInsets): UiRect =
        UiRect(
            x + insents.left,
            y + insents.top,
            (width - insents.horizontal).coerceAtLeast(0),
            (height - insents.vertical).coerceAtLeast(0)
        )

    fun contains(pX: Number, pY: Number): Boolean {
        val c0 = pX.toDouble() >= x.toDouble()
        val c1 = pX.toDouble() <= (x + width).toDouble()
        val c2 = pY.toDouble() >= y.toDouble()
        val c3 = pY.toDouble() <= (y + height)

        return c0 && c1 && c2 && c3
    }
}
