package ru.benos.libs.bos_ui_layout.client.builders

import ru.benos.libs.bos_ui_layout.client.UiDsl
import ru.benos.libs.bos_ui_layout.client.datas.UiBoxTheme
import ru.benos.libs.bos_ui_layout.client.datas.UiGridChild
import ru.benos.libs.bos_ui_layout.client.datas.UiModifier
import ru.benos.libs.bos_ui_layout.client.nodes.UiBoxNode

@UiDsl
class UiGridBuilder {
    private val children: MutableList<UiGridChild> = mutableListOf()

    fun build(): List<UiGridChild> =
        children.toList()

    fun cell(row: Int, column: Int, enableScissor: Boolean = false, block: UiBuilder.() -> Unit) {
        val children = UiBuilder().apply(block).build()
        val node = UiBoxNode(UiBoxTheme.TRANSPARENT, enableScissor, children, UiModifier)

        this.children += UiGridChild(row, column, node)
    }
}