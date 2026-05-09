package ru.benos.libs.bos_ui_layout.api

import ru.benos.libs.bos_ui_layout.datas.IUiStretch

object UiStretch {
    fun fixed(value: Int) =
        IUiStretch.Fixed(value)

    fun fill(weight: Float = 1f) =
        IUiStretch.Fill(weight)

    fun available(weight: Float = 1f) =
        IUiStretch.Available(weight)

    fun expand(weight: Float = 1f) =
        IUiStretch.Expand(weight)
}