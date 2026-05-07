package ru.benos.libs.bos_ui_layout.client.nodes.grid

import ru.benos.libs.bos_ui_layout.client.UiDsl
import ru.benos.libs.bos_ui_layout.client.UiRuntime
import ru.benos.libs.bos_ui_layout.client.datas.IUiStretch
import ru.benos.libs.bos_ui_layout.client.datas.UiModifier
import ru.benos.libs.bos_ui_layout.client.datas.UiRect
import ru.benos.libs.bos_ui_layout.client.datas.UiSize
import ru.benos.libs.bos_ui_layout.client.enum.UiAxis
import ru.benos.libs.bos_ui_layout.client.nodes.AbstractChildrenUiNode
import ru.benos.libs.bos_ui_layout.client.nodes.IUiNode

@UiDsl
class UiLinearLayoutNode(
    private val axis: UiAxis,
    private val gap: Int,

    override val enableScissor: Boolean,
    override val children: List<IUiNode>,
    override val modifier: UiModifier
): AbstractChildrenUiNode() {
    override fun measure(runtime: UiRuntime, availableSize: UiSize): UiSize {
        val bounds = UiRect(0, 0, availableSize.width, availableSize.height).shrink(modifier.padding)
        val measured = children.map { it.measure(runtime, UiSize(bounds.width, bounds.height)) }
        val content = axis.calcContentSize(measured, gap)
        return modifier.resolveSize(content.width, content.height, availableSize)
    }

    override fun renderChildren(runtime: UiRuntime, bounds: UiRect) {
        val measured = children.map { it.measure(runtime, UiSize(bounds.width, bounds.height)) }

        val mainAxis: Int = axiz(bounds.width, bounds.height)
        val fixedSize = measured.zip(children).sumOf { (size, node) ->
            val rule = axiz(node.modifier.stretchSize.width, node.modifier.stretchSize.height)
            when (rule) {
                is IUiStretch.Fill, is IUiStretch.Available -> 0

                else -> axiz(size.width, size.height)
            }
        }

        val totalGap  = gap * (children.size - 1).coerceAtLeast(0)
        val remaining = (mainAxis - fixedSize - totalGap).coerceAtLeast(0)
        val fillWeight = children.sumOf { node ->
            when (val rule = axiz(node.modifier.stretchSize.width, node.modifier.stretchSize.height)) {
                is IUiStretch.Weighted -> rule.weight.toDouble()
                else -> 0.0
            }
        }

        var cursor = axiz(bounds.x, bounds.y)

        children.zip(measured).forEach { (node, size) ->
            val mainRule  = axiz(node.modifier.stretchSize.width, node.modifier.stretchSize.height)
            val crossRule = axiz(node.modifier.stretchSize.height, node.modifier.stretchSize.width)

            val mainSize =
                when (mainRule) {
                    is IUiStretch.Fill, is IUiStretch.Available ->
                        if (fillWeight <= 0.0)
                            0
                        else
                            (remaining * (mainRule.weight.toDouble() / fillWeight)).toInt()
                    else -> axiz(size.width, size.height)
                }
                    .coerceAtLeast(axiz(node.modifier.minSize.width.result, node.modifier.minSize.height.result))

            val crossSize =
                when (crossRule) {
                    is IUiStretch.Fill, is IUiStretch.Available -> axiz(bounds.height, bounds.width)
                    is IUiStretch.Expand                      -> axiz(size.height, size.width)
                    else -> axiz(
                        size.height.coerceAtMost(bounds.height),
                        size.width.coerceAtMost(bounds.width)
                    )
                }
                    .coerceAtLeast(axiz(node.modifier.minSize.width.result, node.modifier.minSize.height.result))

            val crossAlign = axiz(
                node.modifier.aligns.horizontal.calcAlign(bounds.height, crossSize),
                node.modifier.aligns.vertical.calcAlign(bounds.width, crossSize)
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