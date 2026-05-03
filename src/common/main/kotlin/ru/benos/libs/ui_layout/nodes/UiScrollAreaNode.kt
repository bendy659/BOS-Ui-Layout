package ru.benos.libs.ui_layout.nodes

import ru.benos.libs.ui_layout.UiDsl
import ru.benos.libs.ui_layout.UiRuntime
import ru.benos.libs.ui_layout.data.*
import kotlin.reflect.KMutableProperty0

@UiDsl
class UiScrollAreaNode(
    private val scrollOffset: KMutableProperty0<Int>,
    private val scrollAxis: UiAxis,
    private val scrollBarWidth: Int,
    private val scrollBarGap: Int,
    private val scrollBarBackgroundTheme: UiBoxTheme,
    private val scrollBarTheme: UiBoxTheme,
    private val child: IUiNode,

    override val modifier: UiModifier
) : AbstractUiNode() {

    override fun measure(runtime: UiRuntime, maxSize: UiSize): UiSize {
        val inner = UiRect(0, 0, maxSize.width, maxSize.height).shrink(modifier.padding)

        val childMaxSize = when (scrollAxis) {
            UiAxis.Vertical   -> UiSize(inner.width - scrollBarWidth - scrollBarGap, Int.MAX_VALUE / 8)
            UiAxis.Horizontal -> UiSize(Int.MAX_VALUE / 8, inner.height - scrollBarWidth - scrollBarGap)
        }

        val childSize = child.measure(runtime, childMaxSize)

        return when (scrollAxis) {
            UiAxis.Vertical   -> UiSize(
                modifier.resolveWidth(inner.width, maxSize.width),
                modifier.resolveHeight(childSize.height.coerceAtMost(inner.height), maxSize.height)
            )
            UiAxis.Horizontal -> UiSize(
                modifier.resolveWidth(childSize.width.coerceAtMost(inner.width), maxSize.width),
                modifier.resolveHeight(inner.height, maxSize.height)
            )
        }
    }

    override fun render(runtime: UiRuntime, bounds: UiRect) {
        val inner = bounds.shrink(modifier.padding)

        val (viewportRect, barRect) =
            when (scrollAxis) {
                UiAxis.Vertical   -> {
                    val vp = UiRect(inner.x, inner.y, inner.width - scrollBarWidth - scrollBarGap, inner.height)
                    val br = UiRect(inner.right - scrollBarWidth, inner.y, scrollBarWidth, inner.height)
                    vp to br
                }
                UiAxis.Horizontal -> {
                    val vp = UiRect(inner.x, inner.y, inner.width, inner.height - scrollBarWidth - scrollBarGap)
                    val br = UiRect(inner.x, inner.bottom - scrollBarWidth, inner.width, scrollBarWidth)
                    vp to br
                }
            }

        val childSize = child.measure(runtime, UiSize(viewportRect.width, Int.MAX_VALUE / 8))

        val maxOffset = when (scrollAxis) {
            UiAxis.Vertical   -> (childSize.height - viewportRect.height).coerceAtLeast(0)
            UiAxis.Horizontal -> (childSize.width  - viewportRect.width).coerceAtLeast(0)
        }

        scrollOffset.set(scrollOffset.get().coerceIn(0, maxOffset))
        val offset = scrollOffset.get()

        // Рендер ребёнка
        val childBounds = when (scrollAxis) {
            UiAxis.Vertical   -> UiRect(viewportRect.x, viewportRect.y - offset, viewportRect.width, childSize.height)
            UiAxis.Horizontal -> UiRect(viewportRect.x - offset, viewportRect.y, childSize.width, viewportRect.height)
        }

        scissor(runtime, viewportRect, true) {
            child.render(runtime, childBounds)
        }

        // Скроллбар — только если есть что скроллить
        if (maxOffset <= 0) return

        val viewportSize = when (scrollAxis) {
            UiAxis.Vertical   -> viewportRect.height
            UiAxis.Horizontal -> viewportRect.width
        }
        val childContentSize = when (scrollAxis) {
            UiAxis.Vertical   -> childSize.height
            UiAxis.Horizontal -> childSize.width
        }

        val thumbSize = ((viewportSize.toFloat() * viewportSize.toFloat()) / childContentSize.toFloat())
            .toInt().coerceIn(12, viewportSize)

        val travel = (viewportSize - thumbSize).coerceAtLeast(0)
        val thumbOffset = ((offset.toFloat() / maxOffset.toFloat()) * travel).toInt()

        val thumbRect = when (scrollAxis) {
            UiAxis.Vertical   -> UiRect(barRect.x, barRect.y + thumbOffset, scrollBarWidth, thumbSize)
            UiAxis.Horizontal -> UiRect(barRect.x + thumbOffset, barRect.y, thumbSize, scrollBarWidth)
        }

        // Track
        UiBoxNode(
            boxTheme = scrollBarBackgroundTheme,
            enableScissor = false,
            children = emptyList(),
            modifier = UiModifier
        ).render(runtime, barRect)

        // Thumb
        UiBoxNode(
            boxTheme = scrollBarTheme,
            enableScissor = false,
            children = emptyList(),
            modifier = UiModifier
        ).render(runtime, thumbRect)

        registerEvents0(runtime, viewportRect, barRect, thumbRect, maxOffset, travel, thumbSize)
    }

    private fun registerEvents0(
        runtime: UiRuntime,
        viewportRect: UiRect,
        barRect: UiRect,
        thumbRect: UiRect,
        maxOffset: Int,
        travel: Int,
        thumbSize: Int
    ) {
        runtime.addScrollRegion(viewportRect) { delta ->
            scrollOffset.set((scrollOffset.get() - (delta * 14).toInt()).coerceIn(0, maxOffset))
        }

        if (maxOffset <= 0) return

        fun applyScrollFromMouse(mouseX: Number, mouseY: Number) {
            if (travel <= 0) { scrollOffset.set(0); return }
            val localPos = when (scrollAxis) {
                UiAxis.Vertical   -> mouseY.toDouble() - viewportRect.y - thumbSize / 2.0
                UiAxis.Horizontal -> mouseX.toDouble() - viewportRect.x - thumbSize / 2.0
            }.coerceIn(0.0, travel.toDouble())
            scrollOffset.set((maxOffset * (localPos / travel)).toInt().coerceIn(0, maxOffset))
        }

        runtime.addMouseClicked(barRect, modifier.transform) { _, mouseX, mouseY ->
            applyScrollFromMouse(mouseX, mouseY)
            true
        }
        runtime.addDragRegion(
            rect = thumbRect,
            onStartDrag = { _, _, _ -> true },
            onDrag = { _, mouseX, mouseY ->
                applyScrollFromMouse(mouseX, mouseY)
                true
            },
            onEndDrag = { _, _, _ -> true }
        )
    }
}
