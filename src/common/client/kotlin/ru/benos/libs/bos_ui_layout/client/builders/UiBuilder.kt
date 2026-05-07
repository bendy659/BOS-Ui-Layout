package ru.benos.libs.bos_ui_layout.client.builders

import net.minecraft.client.gui.GuiGraphics
import ru.benos.libs.bos_ui_layout.client.UiDsl
import ru.benos.libs.bos_ui_layout.client.datas.UiBoxTheme
import ru.benos.libs.bos_ui_layout.client.datas.UiModifier
import ru.benos.libs.bos_ui_layout.client.datas.UiRect
import ru.benos.libs.bos_ui_layout.client.nodes.IUiNode
import ru.benos.libs.bos_ui_layout.client.nodes.UiBoxNode
import ru.benos.libs.bos_ui_layout.client.nodes.UiRenderNode

@UiDsl
class UiBuilder {
    private val children: MutableList<IUiNode> = mutableListOf()

    fun build(): List<IUiNode> =
        children.toList()

    fun render(
        block: (GuiGraphics, UiRect) -> Unit,
        modifier: UiModifier = UiModifier
    ) {
        children += UiRenderNode(block, modifier)
    }

    fun box(
        boxTheme: UiBoxTheme = UiBoxTheme.TRANSPARENT,
        enableScissor: Boolean = false,
        modifier: UiModifier = UiModifier,
        block: UiBuilder.() -> Unit
    ) {
        val children = UiBuilder().apply(block).build()
        val node = UiBoxNode(boxTheme, enableScissor, children, modifier)

        this.children += node
    }
}