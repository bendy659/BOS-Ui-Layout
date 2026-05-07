package ru.benos.libs.bos_ui_layout.client.nodes

import net.minecraft.client.gui.GuiGraphics
import ru.benos.libs.bos_ui_layout.client.UiDsl
import ru.benos.libs.bos_ui_layout.client.UiRuntime
import ru.benos.libs.bos_ui_layout.client.datas.UiModifier
import ru.benos.libs.bos_ui_layout.client.datas.UiRect

@UiDsl
class UiRenderNode(
    val block: (GuiGraphics, UiRect) -> Unit,

    override val modifier: UiModifier
) : AbstractUiNode() {
    override fun render(runtime: UiRuntime, bounds: UiRect) =
        block(runtime.guiGraphics, bounds)
}