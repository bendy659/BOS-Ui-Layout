package ru.benos.libs.ui_layout.nodes

import ru.benos.libs.ui_layout.UiDsl
import ru.benos.libs.ui_layout.UiRuntime
import ru.benos.libs.ui_layout.data.*
import kotlin.math.max

@UiDsl
open class UiBoxNode(
    protected val boxTheme     : UiBoxTheme,

    override val enableScissor: Boolean,
    override val children: List<IUiNode>,
    override val modifier: UiModifier
): AbstractUiChildrenNode() {
    override fun measure(runtime: UiRuntime, maxSize: UiSize): UiSize {
        val inner = UiRect(0, 0, maxSize.width, maxSize.height)
            .shrink(modifier.padding)

        var contentWidth = 0
        var contentHeight = 0

        children.forEach { child ->
            val measured = child.measure(runtime, maxSize)

            contentWidth  = max(contentWidth, measured.width)
            contentHeight = max(contentHeight, measured.height)
        }

        return UiSize(
            modifier.resolveWidth(contentWidth, maxSize.width),
            modifier.resolveHeight(contentHeight, maxSize.height)
        )
    }

    override fun renderChildren(runtime: UiRuntime, bounds: UiRect) {
        children.forEach { node ->
            val measured = node.measure(runtime, UiSize(bounds.width, bounds.height))

            val childWidth  = calcUiLength(node.modifier.width, bounds.width, runtime.currentAvailableWidth, measured.width)
            val childHeight = calcUiLength(node.modifier.height, bounds.height, runtime.currentAvailableHeight, measured.height)

            val hAlignOffset = calcAlign(modifier.hAlign, bounds.width, childWidth)
            val vAlignOffset = calcAlign(modifier.vAlign, bounds.height, childHeight)

            val childBounds = UiRect(bounds.x + hAlignOffset, bounds.y + vAlignOffset, measured.width, measured.height)
            node.render(runtime, childBounds)
        }
    }

    override fun renderBackground(runtime: UiRuntime, bounds: UiRect) {
        val backgroundColor =
            when {
                runtime.isClicked(bounds)  -> boxTheme.layersClicked
                runtime.isHovered(bounds)  -> boxTheme.layersHovered
                runtime.isReleased(bounds) -> boxTheme.layersReleased
                //isFocused       -> boxTheme.backgroundFocused

                else -> boxTheme.layersNormal
            }

        // Background //
        backgroundColor.forEach { it.render(runtime.guiGraphics, bounds) }
    }
}