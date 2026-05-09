package ru.benos.libs.bos_ui_layout.builders

import ru.benos.libs.bos_ui_layout.UiDsl
import ru.benos.libs.bos_ui_layout.datas.UiBoxTheme
import ru.benos.libs.bos_ui_layout.datas.UiGridChild
import ru.benos.libs.bos_ui_layout.datas.UiModifier
import ru.benos.libs.bos_ui_layout.nodes.UiBoxNode

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