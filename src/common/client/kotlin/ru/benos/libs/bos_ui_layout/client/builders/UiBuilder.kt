package ru.benos.libs.bos_ui_layout.client.builders

import net.minecraft.client.gui.GuiGraphics
import ru.benos.libs.bos_ui_layout.client.UiDsl
import ru.benos.libs.bos_ui_layout.client.datas.UiModifier
import ru.benos.libs.bos_ui_layout.client.datas.UiRect
import ru.benos.libs.bos_ui_layout.client.nodes.IUiNode
import ru.benos.libs.bos_ui_layout.client.nodes.UiRenderNode

@UiDsl
class UiBuilder {
    private val children: MutableList<IUiNode> = mutableListOf()

    fun render(
        block: (GuiGraphics, UiRect) -> Unit,
        modifier: UiModifier = UiModifier
    ) {
        children += UiRenderNode(block, modifier)
    }
}