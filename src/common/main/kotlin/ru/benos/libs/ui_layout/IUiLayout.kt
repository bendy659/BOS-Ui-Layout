package ru.benos.libs.ui_layout

import ru.benos.libs.ui_layout.builder.UiBuilder
import ru.benos.libs.ui_layout.data.UiRect
import ru.benos.libs.ui_layout.nodes.IUiNode

interface IUiLayout {
    var runtime: UiRuntime?
    val contentBounds: UiRect

    fun UiBuilder.ui()

    fun buildUi(children: UiBuilder.() -> Unit): IUiNode
}