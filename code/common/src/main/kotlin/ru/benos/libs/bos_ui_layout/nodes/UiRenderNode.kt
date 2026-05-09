package ru.benos.libs.bos_ui_layout.nodes

import net.minecraft.client.gui.GuiGraphics
import ru.benos.libs.bos_ui_layout.UiDsl
import ru.benos.libs.bos_ui_layout.UiRuntime
import ru.benos.libs.bos_ui_layout.datas.UiModifier
import ru.benos.libs.bos_ui_layout.datas.base.UiRect

@UiDsl
class UiRenderNode(
    val block: (GuiGraphics, UiRect) -> Unit,

    override val modifier: UiModifier
) : AbstractUiNode() {
    override fun render(runtime: UiRuntime, bounds: UiRect) =
        block(runtime.guiGraphics, bounds)
}