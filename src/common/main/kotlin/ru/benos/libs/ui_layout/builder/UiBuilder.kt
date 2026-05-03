package ru.benos.libs.ui_layout.builder

import net.minecraft.network.chat.Component
import ru.benos.libs.ui_layout.data.UiAlign
import ru.benos.libs.ui_layout.data.UiAxis
import ru.benos.libs.ui_layout.data.UiBoxTheme
import ru.benos.libs.ui_layout.data.UiModifier
import ru.benos.libs.ui_layout.nodes.IUiNode
import ru.benos.libs.ui_layout.nodes.UiBoxNode
import ru.benos.libs.ui_layout.nodes.UiLabelNode
import ru.benos.libs.ui_layout.nodes.UiScrollAreaNode
import ru.benos.libs.ui_layout.nodes.grid.UiLinearLayoutNode
import kotlin.reflect.KMutableProperty0

open class UiBuilder {
    private val children: MutableList<IUiNode> = mutableListOf()

    companion object: UiBuilder()

    fun build(): List<IUiNode> =
        children.toList()

    // Nodes //

    fun box(
        boxTheme: UiBoxTheme = UiBoxTheme.TRANSPARENT,
        enableScissor: Boolean = false,
        modifier: UiModifier = UiModifier,
        block: UiBuilder.() -> Unit = { }
    ) {
        val children = UiBuilder.apply(block).build()
        val node = UiBoxNode(boxTheme, enableScissor, children, modifier)
        this.children.add(node)
    }

    fun label(
        component: Component,
        enableScissor: Boolean = false,
        textAlign: UiAlign = UiAlign.Start,
        wrap: Boolean = false,
        maxLines: Int = Int.MAX_VALUE,
        enableLabelShadow: Boolean = true,
        modifier: UiModifier = UiModifier
    ) {
        val node = UiLabelNode(component, enableScissor, textAlign, wrap, maxLines, enableLabelShadow, modifier)
        this.children.add(node)
    }

    fun progressBar(
        boxTheme: UiBoxTheme = UiBoxTheme.BLACK,
        boxModifier: UiModifier = UiModifier,
        barTheme: UiBoxTheme = UiBoxTheme.RED,
        barAlign: UiAlign = UiAlign.Start,
        barOverlay: UiBuilder.() -> Unit = { }
    ) {
        box(boxTheme, true, boxModifier) {
            val barModifier = UiModifier
                .hAlign(barAlign)
                .availableWidth()
                .availableHeight()

            box(barTheme, false, barModifier)

            barOverlay()
        }
    }

    private fun UiBuilder.linearLayout(
        axis: UiAxis,
        gap: Int,
        enableScissor: Boolean,
        modifier: UiModifier,
        block: UiBuilder.() -> Unit
    ) {
        val children = UiBuilder.apply(block).build()
        val node = UiLinearLayoutNode(axis, gap ,enableScissor, children, modifier)
        this.children.add(node)
    }

    fun row(
        gap: Int = 0,
        enableScissor: Boolean = true,
        modifier: UiModifier = UiModifier,
        block: UiBuilder.() -> Unit
    ) =
        linearLayout(UiAxis.Horizontal, gap, enableScissor, modifier, block)

    fun column(
        gap: Int = 0,
        enableScissor: Boolean = true,
        modifier: UiModifier = UiModifier,
        block: UiBuilder.() -> Unit
    ) =
        linearLayout(UiAxis.Vertical, gap, enableScissor, modifier, block)

    fun scrollArea(
        scrollOffset: KMutableProperty0<Int>,
        scrollAxis: UiAxis = UiAxis.Vertical,
        scrollBarWidth: Int = 4,
        scrollBarGap: Int = 0,
        scrollBarBackgroundTheme: UiBoxTheme = UiBoxTheme.BLACK,
        scrollBarTheme: UiBoxTheme = UiBoxTheme.RED,
        boxContentTheme: UiBoxTheme = UiBoxTheme.TRANSPARENT,
        modifier: UiModifier = UiModifier,
        block: UiBuilder.() -> Unit
    ) {
        val children = UiBuilder.apply(block).build()
        val boxModifier = UiModifier
            .availableWidth()
            .availableHeight()
        val child = UiBoxNode(boxContentTheme, true, children, boxModifier)
        val node = UiScrollAreaNode(scrollOffset, scrollAxis, scrollBarWidth, scrollBarGap, scrollBarBackgroundTheme, scrollBarTheme, child, modifier)

        this.children.add(node)
    }
}