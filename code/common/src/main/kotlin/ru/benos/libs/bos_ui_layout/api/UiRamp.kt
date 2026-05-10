package ru.benos.libs.bos_ui_layout.api

import ru.benos.libs.bos_ui_layout.datas.base.UiColor
import ru.benos.libs.bos_ui_layout.datas.ramp.AbstractUiRamp
import ru.benos.libs.bos_ui_layout.datas.ramp.UiRampColor
import ru.benos.libs.bos_ui_layout.datas.ramp.UiRampFloat

object UiRamp {
    fun float(block: AbstractUiRamp.UiRampBuilder<Float>.() -> Unit): UiRampFloat =
        UiRampFloat(block)

    fun color(block: AbstractUiRamp.UiRampBuilder<UiColor>.() -> Unit): UiRampColor =
        UiRampColor(block)
}