package ru.benos.libs.bos_ui_layout.client.builders

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import ru.benos.libs.bos_ui_layout.client.UiDsl
import ru.benos.libs.bos_ui_layout.client.api.UiCanvas
import ru.benos.libs.bos_ui_layout.client.datas.UiBoxTheme
import ru.benos.libs.bos_ui_layout.client.datas.UiColor
import ru.benos.libs.bos_ui_layout.client.datas.UiModifier
import ru.benos.libs.bos_ui_layout.client.datas.UiRect
import ru.benos.libs.bos_ui_layout.client.enum.UiTextAlign
import ru.benos.libs.bos_ui_layout.client.nodes.IUiNode
import ru.benos.libs.bos_ui_layout.client.nodes.UiBoxNode
import ru.benos.libs.bos_ui_layout.client.nodes.UiLabelNode
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
        textAlign: UiTextAlign,
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
        color: UiColor,
        resource: String,
        modifier: UiModifier = UiModifier,
        block: UiBuilder.() -> Unit = { }
    ) {
        val layer = setOf(UiCanvas.texture(color, resource))
        val boxTheme = UiBoxTheme(layer)

        box(boxTheme, false, modifier, block)
    }
}