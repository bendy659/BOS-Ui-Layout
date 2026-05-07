package ru.benos.libs.bos_ui_layout.client.datas

data class UiColor(var r: Int, var g: Int, var b: Int, var a: Int) {
    val int: Int
        get() = (a shl 24) or (r shl 16) or (g shl 8) or b

    val rFloat: Float
        get() = r.toFloat() / 255.0f
    val gFloat: Float
        get() = g.toFloat() / 255.0f
    val bFloat: Float
        get() = b.toFloat() / 255.0f
    val aFloat: Float
        get() = a.toFloat() / 255.0f

    companion object {
        val WHITE: UiColor = UiColor(255, 255, 255, 255)
        val BLACK: UiColor = UiColor(0, 0, 0, 255)
        val RED  : UiColor = UiColor(255, 0, 0, 255)
        val GREEN: UiColor = UiColor(0, 255, 0, 255)
        val BLUE : UiColor = UiColor(0, 0, 255, 255)

        fun fromHex(hex: Long): UiColor {
            val a = ((hex shr 24) and 0xFF).toInt()
            val r = ((hex shr 16) and 0xFF).toInt()
            val g = ((hex shr 8) and 0xFF).toInt()
            val b = (hex and 0xFF).toInt()

            return UiColor(r, g, b, a)
        }

        fun fromHex(hex: Int): UiColor {
            val r = (hex shr 16) and 0xFF
            val g = (hex shr 8) and 0xFF
            val b = hex and 0xFF

            return UiColor(r, g, b, 255)
        }
    }

    fun brightness(factor: Float): UiColor {
        this.r = (r * factor).toInt().coerceIn(0, 255)
        this.g = (g * factor).toInt().coerceIn(0, 255)
        this.b = (b * factor).toInt().coerceIn(0, 255)

        return this
    }

    fun contrast(factor: Float): UiColor {
        this.r = ((r - 128) * factor + 128).toInt().coerceIn(0, 255)
        this.g = ((g - 128) * factor + 128).toInt().coerceIn(0, 255)
        this.b = ((b - 128) * factor + 128).toInt().coerceIn(0, 255)

        return this
    }
}