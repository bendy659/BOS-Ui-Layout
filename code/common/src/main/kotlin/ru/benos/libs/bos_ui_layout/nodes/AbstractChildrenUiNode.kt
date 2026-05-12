package ru.benos.libs.bos_ui_layout.nodes

import ru.benos.libs.bos_ui_layout.UiRuntime
import ru.benos.libs.bos_ui_layout.datas.IUiStretch
import ru.benos.libs.bos_ui_layout.datas.base.UiRect
import ru.benos.libs.bos_ui_layout.datas.base.UiSize
import kotlin.math.max

abstract class AbstractChildrenUiNode : AbstractUiNode() {
    abstract val children: List<IUiNode>

    override fun measure(runtime: UiRuntime, availableSize: UiSize): UiSize {
        val inner = UiRect(0, 0, availableSize).shrink(modifier.padding)

        var contentWidth = 0
        var contentHeight = 0

        children.forEach { child ->
            val measured = child.measure(runtime, UiSize(inner.width, inner.height))

            if (child.modifier.stretchSize.width !is IUiStretch.Fill &&
                child.modifier.stretchSize.width !is IUiStretch.Expand)
                contentWidth = max(contentWidth, measured.width)

            if (child.modifier.stretchSize.height !is IUiStretch.Fill &&
                child.modifier.stretchSize.height !is IUiStretch.Expand)
                contentHeight = max(contentHeight, measured.height)
        }

        return modifier.resolveSize(contentWidth, contentHeight, availableSize)
            .applyTransformLayout()
    }

    override fun render(runtime: UiRuntime, bounds: UiRect) {
        super.render(runtime, bounds)

        transformative(runtime, bounds) {
            val inner = bounds.shrink(modifier.padding)
            scissor(runtime, inner) { renderChildren(runtime, inner) }
        }
    }

    protected open fun renderChildren(runtime: UiRuntime, bounds: UiRect) {
        children.forEach { child ->
            val measured = child.measure(runtime, bounds.size)

            val childSize = UiSize(
                child.modifier.stretchSize.width.calcLength(bounds.width, runtime.currentAvailableWidth, measured.width),
                child.modifier.stretchSize.height.calcLength(bounds.height, runtime.currentAvailableHeight, measured.height)
            )
            val (hAlignOffset, vAlignOffset) = child.modifier.aligns.calcAligns(bounds.size, childSize)
            val overridePosition = child.modifier.overridePosition

            val childBounds = UiRect(
                overridePosition?.let { bounds.x + it.x } ?: bounds.x + hAlignOffset,
                overridePosition?.let { bounds.y + it.y } ?: bounds.y + vAlignOffset,
                childSize
            )
            child.render(runtime, childBounds)
        }
    }
}
