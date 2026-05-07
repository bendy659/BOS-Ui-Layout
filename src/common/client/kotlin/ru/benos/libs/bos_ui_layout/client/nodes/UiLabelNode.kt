package ru.benos.libs.bos_ui_layout.client.nodes

import net.minecraft.network.chat.Component
import net.minecraft.util.FormattedCharSequence
import ru.benos.libs.bos_ui_layout.client.UiDsl
import ru.benos.libs.bos_ui_layout.client.UiRuntime
import ru.benos.libs.bos_ui_layout.client.datas.UiModifier
import ru.benos.libs.bos_ui_layout.client.datas.UiRect
import ru.benos.libs.bos_ui_layout.client.datas.UiSize
import ru.benos.libs.bos_ui_layout.client.enum.UiTextAlign

@UiDsl
class UiLabelNode(
    private val component: Component,
    private val textAlign: UiTextAlign,
    private val wrap: Boolean,
    private val maxLines: Int,
    private val enableLabelShadow: Boolean,

    override val modifier: UiModifier
): AbstractUiNode() {
    override fun measure(runtime: UiRuntime, availableSize: UiSize): UiSize {
        val innerWidth = (availableSize.width - modifier.padding.horizontal).coerceAtLeast(0)
        val lines = measureLines(runtime, innerWidth)

        val contentWidth = lines.maxOfOrNull(runtime.font::width) ?: 0
        val contentHeight = lines.size * runtime.font.lineHeight

        return modifier.resolveSize(contentWidth, contentHeight, availableSize)
    }

    override fun render(runtime: UiRuntime, bounds: UiRect) {
        super.render(runtime, bounds)

        val inner = bounds.shrink(modifier.padding)
        val lines = measureLines(runtime, inner.width)

        lines.forEachIndexed { index, line ->
            val lineWidth = runtime.font.width(line)
            val drawX = textAlign.calcOffsetX(inner, lineWidth)
            val drawY = inner.y + (index * runtime.font.lineHeight)

            runtime.guiGraphics.drawString(
                runtime.font,
                line,
                drawX, drawY,
                0xFFFFFFFF.toInt(),
                enableLabelShadow
            )
        }
    }

    private fun measureLines(runtime: UiRuntime, availableWidth: Int): List<FormattedCharSequence> {
        if (!wrap || availableWidth <= 0)
            return listOf(component.visualOrderText)

        val wrapped = runtime.font.split(component, availableWidth)
            .take(maxLines.coerceAtLeast(1))

        return wrapped.ifEmpty { listOf(component.visualOrderText) }
    }
}