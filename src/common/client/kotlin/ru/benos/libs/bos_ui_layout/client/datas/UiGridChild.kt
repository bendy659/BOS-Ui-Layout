package ru.benos.libs.bos_ui_layout.client.datas

import ru.benos.libs.bos_ui_layout.client.nodes.IUiNode

data class UiGridChild(
    var row: Int,
    var column: Int,
    val node: IUiNode
)
