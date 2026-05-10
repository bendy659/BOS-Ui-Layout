package ru.benos.libs.bos_ui_layout.datas.ramp

class UiRampFloat : AbstractUiRamp<Float> {
    constructor(block: UiRampBuilder<Float>.() -> Unit) :
            super(UiRampBuilder<Float>().apply(block).buildKeys())

    override fun lerp(t: Float, a: Float, b: Float) = a + (b - a) * t
}