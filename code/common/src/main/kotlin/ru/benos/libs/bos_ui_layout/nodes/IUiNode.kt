package ru.benos.libs.bos_ui_layout.nodes

import ru.benos.libs.bos_ui_layout.UiRuntime
import ru.benos.libs.bos_ui_layout.datas.UiModifier
import ru.benos.libs.bos_ui_layout.datas.base.UiRect
import ru.benos.libs.bos_ui_layout.datas.base.UiSize

interface IUiNode {
    val modifier: UiModifier

    fun measure(runtime: UiRuntime, availableSize: UiSize): UiSize
    fun render(runtime: UiRuntime, bounds: UiRect)
}