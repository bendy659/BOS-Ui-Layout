package ru.benos.libs.bos_ui_layout.datas

import ru.benos.libs.bos_ui_layout.nodes.IUiNode

data class UiGridChild(
    var row: Int,
    var column: Int,
    val node: IUiNode
)
