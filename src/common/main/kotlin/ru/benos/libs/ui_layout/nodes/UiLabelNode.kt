package ru.benos.libs.ui_layout.nodes

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.util.FormattedCharSequence
import ru.benos.libs.ui_layout.UiDsl
import ru.benos.libs.ui_layout.UiRuntime
import ru.benos.libs.ui_layout.data.UiAlign
import ru.benos.libs.ui_layout.data.UiModifier
import ru.benos.libs.ui_layout.data.UiRect
import ru.benos.libs.ui_layout.data.UiSize

@UiDsl
open class UiLabelNode(
    private val component        : Component,
    private val enableScissor    : Boolean,
    private val textAlign        : UiAlign,
    private val wrap             : Boolean,
    private val maxLines         : Int,
    private val enableLabelShadow: Boolean,

    override val modifier: UiModifier
) : AbstractUiNode() {
    override fun measure(runtime: UiRuntime, maxSize: UiSize): UiSize {
        val innerWidth = (maxSize.width - modifier.padding.horizontal).coerceAtLeast(0)
        val lines = measureLines(runtime, innerWidth)

        val width = lines.maxOfOrNull(runtime.font::width) ?: 0
        val height = lines.size * runtime.font.lineHeight

        return UiSize(
            modifier.resolveWidth(width, maxSize.width),
            modifier.resolveHeight(height, maxSize.height)
        )
    }

    override fun render(runtime: UiRuntime, bounds: UiRect) {
        val transformedBounds = modifier.transform.applyToBounds(bounds, modifier.affectOffset, modifier.affectRotation, modifier.affectScale)
        super.render(runtime, bounds)

        renderTransformed(runtime.guiGraphics, bounds) {
            scissor(runtime, transformedBounds, enableScissor) {
                renderLabel(runtime, transformedBounds)
            }
        }
    }

    private fun renderLabel(runtime: UiRuntime, bounds: UiRect) {
        val inner = bounds.shrink(modifier.padding)
        val lines = measureLines(runtime, inner.width)

        lines.forEachIndexed { index, line ->
            val lineWidth = runtime.font.width(line)
            val drawX =
                when (textAlign) {
                    UiAlign.Start  -> inner.x
                    UiAlign.Center -> inner.x + ((inner.width - lineWidth) / 2).coerceAtLeast(0)
                    UiAlign.End    -> inner.right - lineWidth
                }
            val drawY = inner.y + (index * runtime.font.lineHeight)

            renderDrawString(runtime.guiGraphics, runtime.font, line, drawX, drawY, enableLabelShadow)
        }
    }

    fun measureLines(runtime: UiRuntime, availableWidth: Int): List<FormattedCharSequence> {
        if (!wrap || availableWidth <= 0)
            return listOf(component.visualOrderText)

        val wrapped = runtime.font.split(component, availableWidth)
            .take(maxLines.coerceAtLeast(1))

        return wrapped.ifEmpty { listOf(component.visualOrderText) }
    }
}

expect fun renderDrawString(guiGraphics: GuiGraphics, font: Font, line: FormattedCharSequence, drawX: Int, drawY: Int, enableLabelShadow: Boolean): Unit