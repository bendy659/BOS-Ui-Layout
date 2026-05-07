package ru.benos.libs.bos_ui_layout.client.nodes

import ru.benos.libs.bos_ui_layout.client.UiRuntime
import ru.benos.libs.bos_ui_layout.client.datas.UiModifier
import ru.benos.libs.bos_ui_layout.client.datas.UiRect
import ru.benos.libs.bos_ui_layout.client.datas.UiSize

interface IUiNode {
    val modifier: UiModifier

    fun measure(runtime: UiRuntime, availableSize: UiSize): UiSize
    fun render(runtime: UiRuntime, bounds: UiRect)
}