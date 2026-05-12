package ru.benos.libs.bos_ui_layout.builders

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import ru.benos.libs.bos_ui_layout.UiDsl
import ru.benos.libs.bos_ui_layout.datas.IUiImageStretch
import ru.benos.libs.bos_ui_layout.enum.UiAxis
import ru.benos.libs.bos_ui_layout.enum.UiTextAlign
import ru.benos.libs.bos_ui_layout.nodes.IUiNode
import ru.benos.libs.bos_ui_layout.nodes.UiBoxNode
import ru.benos.libs.bos_ui_layout.nodes.UiImageNode
import ru.benos.libs.bos_ui_layout.nodes.UiLabelNode
import ru.benos.libs.bos_ui_layout.nodes.UiRenderNode
import ru.benos.libs.bos_ui_layout.nodes.grid.UiGridNode
import ru.benos.libs.bos_ui_layout.nodes.grid.UiLinearLayoutNode
import ru.benos.libs.bos_ui_layout.datas.UIScrollState
import ru.benos.libs.bos_ui_layout.datas.UiBoxTheme
import ru.benos.libs.bos_ui_layout.datas.base.UiColor
import ru.benos.libs.bos_ui_layout.datas.UiModifier
import ru.benos.libs.bos_ui_layout.datas.base.UiRect

@UiDsl
open class UiBuilder {
    private val children: MutableList<IUiNode> = mutableListOf()

    fun build(): List<IUiNode> =
        children.toList()

    fun render(
        block: (GuiGraphics, UiRect) -> Unit,
        modifier: UiModifier = UiModifier
    ) {
        val node = UiRenderNode(block, modifier)

        this.children += node
    }

    fun box(
        boxTheme: UiBoxTheme = UiBoxTheme.TRANSPARENT,
        enableScissor: Boolean = false,
        modifier: UiModifier = UiModifier,
        block: UiBuilder.() -> Unit = { }
    ) {
        val children = UiBuilder().apply(block).build()
        val node = UiBoxNode(boxTheme, enableScissor, children, modifier)

        this.children += node
    }

    fun label(
        component: Component,
        textAlign: UiTextAlign = UiTextAlign.Left,
        wrap: Boolean = false,
        maxLines: Int = Int.MAX_VALUE,
        enableLabelShadow: Boolean = true,
        modifier: UiModifier = UiModifier
    ) {
        val node = UiLabelNode(component, textAlign, wrap, maxLines, enableLabelShadow, modifier)

        this.children += node
    }

    fun button(
        boxTheme: UiBoxTheme = UiBoxTheme.DEFAULT,
        modifier: UiModifier = UiModifier,
        onClick: (Int, Int, Int) -> Boolean,
        block: UiBuilder.() -> Unit = { }
    ) {
        val buttonModifier = modifier
            .mouseEvents { onReleased(onClick) }

        box(boxTheme, false, buttonModifier, block)
    }

    fun image(
        resource: String,
        multiplyColor: UiColor = UiColor.WHITE,
        uv: UiRect? = null,
        stretch: IUiImageStretch = IUiImageStretch.Stretch,
        modifier: UiModifier = UiModifier
    ) {
        val node = UiImageNode(resource, multiplyColor, uv, stretch, modifier)

        this.children += node
    }

    fun row(
        gap: Int = 0,
        modifier: UiModifier = UiModifier,
        block: UiBuilder.() -> Unit = { }
    ) =
        linearLayout(UiAxis.Horizontal, gap, modifier, block)

    fun column(
        gap: Int = 0,
        modifier: UiModifier = UiModifier,
        block: UiBuilder.() -> Unit = { }
    ) =
        linearLayout(UiAxis.Vertical, gap, modifier, block)

    fun grid(
        rows: Int,
        columns: Int,
        hGap: Int = 0,
        vGap: Int = 0,
        modifier: UiModifier = UiModifier,
        block: UiGridBuilder.() -> Unit = { }
    ) {
        val children = UiGridBuilder().apply(block).build()
        val node = UiGridNode(rows, columns, hGap, vGap, children, modifier)

        this.children += node
    }

    //// Private ////

    private fun linearLayout(
        axis: UiAxis,
        gap: Int,
        modifier: UiModifier = UiModifier,
        block: UiBuilder.() -> Unit = { }
    ) {
        val children = UiBuilder().apply(block).build()
        val node = UiLinearLayoutNode(axis, gap, false, children, modifier)

        this.children += node
    }

    private fun scrollbar(state: UIScrollState, axis: UiAxis, barTheme: UiBoxTheme, backgroundBarTheme: UiBoxTheme) {

    }
}
