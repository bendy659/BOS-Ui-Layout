package ru.benos.libs.bos_ui_layout.datas.ramp

data class UiRampKey<T>(
    val time: Float,
    val value: T,
    val easing: Float = 1f
)
