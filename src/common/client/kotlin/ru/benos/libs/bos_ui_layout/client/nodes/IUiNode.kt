package ru.benos.libs.bos_ui_layout.client.nodes

import ru.benos.libs.bos_ui_layout.client.UiRuntime
import ru.benos.libs.bos_ui_layout.client.datas.UiRect
import ru.benos.libs.bos_ui_layout.client.datas.UiSize

interface IUiNode {
    val nodeBounds: UiSize

    fun render(runtime: UiRuntime, bounds: UiRect)
}