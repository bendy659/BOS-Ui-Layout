package ru.benos.libs.ui_layout.nodes.grid

import ru.benos.libs.ui_layout.UiDsl
import ru.benos.libs.ui_layout.UiRuntime
import ru.benos.libs.ui_layout.data.*
import ru.benos.libs.ui_layout.nodes.AbstractUiChildrenNode
import ru.benos.libs.ui_layout.nodes.IUiNode

@UiDsl
open class UiLinearLayoutNode(
    protected val axis: UiAxis,
    protected val gap: Int,

    override val enableScissor: Boolean,
    override val children: List<IUiNode>,
    override val modifier: UiModifier
): AbstractUiChildrenNode() {
    override fun measure(runtime: UiRuntime, maxSize: UiSize): UiSize {
        val bounds = UiRect(0, 0, maxSize.width, maxSize.height).shrink(modifier.padding)
        val measured = children.map { it.measure(runtime, UiSize(bounds.width, bounds.height)) }

        val contentWidth : Int
        val contentHeight: Int
        val measureGap = gap * (children.size - 1)

        when (axis) {
            UiAxis.Horizontal -> {
                contentWidth  = measured.sumOf(UiSize::width) + measureGap
                    .coerceAtLeast(0)
                contentHeight = measured.maxOfOrNull(UiSize::height) ?: 0
            }
            UiAxis.Vertical   -> {
                contentWidth  = measured.maxOfOrNull(UiSize::width) ?: 0
                contentHeight = measured.sumOf(UiSize::height) + measureGap
                    .coerceAtLeast(0)
            }
        }

        return UiSize(
            modifier.resolveWidth(contentWidth, maxSize.width),
            modifier.resolveHeight(contentHeight, maxSize.height)
        )
    }

    override fun renderChildren(runtime: UiRuntime, bounds: UiRect) {
        val measured = children.map { it.measure(runtime, UiSize(bounds.width, bounds.height)) }

        val mainAxis: Int = axiz(bounds.width, bounds.height)
        val fixedSize = measured.zip(children).sumOf { (size, node) ->
            val rule = axiz(node.modifier.width, node.modifier.height)
            when (rule) {
                is IUiLength.Fill, is IUiLength.Available -> 0

                else -> axiz(size.width, size.height)
            }
        }

        val totalGap  = gap * (children.size - 1).coerceAtLeast(0)
        val remaining = (mainAxis - fixedSize - totalGap).coerceAtLeast(0)
        val fillWeight = children.sumOf { node ->
            when (val rule = axiz(node.modifier.width, node.modifier.height)) {
                is IUiLength.IUiWeighted -> rule.weight.toDouble()
                else -> 0.0
            }
        }

        var cursor = axiz(bounds.x, bounds.y)

        children.zip(measured).forEach { (node, size) ->
            val mainRule  = axiz(node.modifier.width, node.modifier.height)
            val crossRule = axiz(node.modifier.height, node.modifier.width)

            val mainSize =
                when (mainRule) {
                    is IUiLength.Fill, is IUiLength.Available ->
                        if (fillWeight <= 0.0)
                            0
                        else
                            (remaining * (mainRule.weight.toDouble() / fillWeight)).toInt()
                    else -> axiz(size.width, size.height)
                }
                    .coerceAtLeast(axiz(node.modifier.minSize.width, node.modifier.minSize.height))

            val crossSize =
                when (crossRule) {
                    is IUiLength.Fill, is IUiLength.Available -> axiz(bounds.height, bounds.width)
                    is IUiLength.Expand                      -> axiz(size.height, size.width)
                    else -> axiz(
                        size.height.coerceAtMost(bounds.height),
                        size.width.coerceAtMost(bounds.width)
                    )
                }
                    .coerceAtLeast(
                        axiz(
                            node.modifier.minSize.width,
                            node.modifier.minSize.height
                        )
                    )

            val crossAlign = axiz(
                calcAlign(node.modifier.vAlign, bounds.height, crossSize),
                calcAlign(node.modifier.hAlign, bounds.width, crossSize)
            )

            val childBound = axiz(
                UiRect(cursor, bounds.y + crossAlign, mainSize, crossSize),
                UiRect(bounds.x + crossAlign, cursor, crossSize, mainSize)
            )

            node.render(runtime, childBound)

            cursor += mainSize + gap
        }
    }

    private fun <T> axiz(horizontal: T, vertical: T): T =
        when (axis) {
            UiAxis.Horizontal -> horizontal
            UiAxis.Vertical   -> vertical
        }
}