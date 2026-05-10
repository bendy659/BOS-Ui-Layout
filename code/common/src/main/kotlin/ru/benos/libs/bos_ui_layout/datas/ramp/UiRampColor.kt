package ru.benos.libs.bos_ui_layout.datas.ramp

import ru.benos.libs.bos_ui_layout.datas.base.UiColor

class UiRampColor: AbstractUiRamp<UiColor> {
    constructor(block: UiRampBuilder<UiColor>.() -> Unit) :
            super(UiRampBuilder<UiColor>().apply(block).buildKeys())

    override fun lerp(t: Float, a: UiColor, b: UiColor): UiColor =
        UiColor(
            r = (a.r + (b.r - a.r) * t).toInt(),
            g = (a.g + (b.g - a.g) * t).toInt(),
            b = (a.b + (b.b - a.b) * t).toInt(),
            a = (a.a + (b.a - a.a) * t).toInt()
        )
}