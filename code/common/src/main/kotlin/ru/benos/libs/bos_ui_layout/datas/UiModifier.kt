package ru.benos.libs.bos_ui_layout.datas

import org.joml.Vector2i
import ru.benos.libs.bos_ui_layout.UiRuntime
import ru.benos.libs.bos_ui_layout.datas.base.UiInsets
import ru.benos.libs.bos_ui_layout.datas.base.UiSize
import ru.benos.libs.bos_ui_layout.datas.base.UiTransform
import ru.benos.libs.bos_ui_layout.datas.events.UiKeyEvents
import ru.benos.libs.bos_ui_layout.datas.events.UiMouseEvents
import ru.benos.libs.bos_ui_layout.datas.events.UiNodeEvents
import ru.benos.libs.bos_ui_layout.enum.UiAlign

open class UiModifier(
    // Basic //

    val minSize: UiSizes = UiSizes.Companion.ZERO,
    val maxSize: UiSizes? = null,
    val padding: UiInsets = UiInsets.Companion.ZERO,

    val overridePosition: Vector2i? = null,

    val stretchSize: UiStretchSize = UiStretchSize.Companion.WRAP,
    val aligns: UiAligns = UiAligns.LEFT_TOP_CORNER,
    val transform: UiTransform = UiTransform.Companion.DEFAULT,

    // Events //

    val mouseEvents: UiMouseEvents = UiMouseEvents.EMPTY,
    val keyEvents: UiKeyEvents = UiKeyEvents.EMPTY,
    val nodeEvents: UiNodeEvents = UiNodeEvents.EMPTY
) {
    companion object : UiModifier()

    fun copy(
        // Basis //

        minSize: UiSizes = this.minSize,
        maxSize: UiSizes? = this.maxSize,
        padding: UiInsets = this.padding,

        overridePosition: Vector2i? = this.overridePosition,

        stretchSize: UiStretchSize = this.stretchSize,
        aligns: UiAligns = this.aligns,
        transform: UiTransform = this.transform,

        // Events //

        mouseEvents: UiMouseEvents = this.mouseEvents,
        keyEvents: UiKeyEvents = this.keyEvents,
        nodeEvents: UiNodeEvents = this.nodeEvents
    ): UiModifier =
        UiModifier(
            minSize, maxSize, padding,
            overridePosition,
            stretchSize, aligns, transform,
            mouseEvents, keyEvents, nodeEvents
        )

    //// Builder /////

    fun minSize(width: IUiSizes, height: IUiSizes): UiModifier =
        this.copy(minSize = UiSizes(width, height))

    fun maxSize(width: IUiSizes, height: IUiSizes): UiModifier =
        this.copy(maxSize = UiSizes(width, height))

    fun padding(
        left: Int = this.padding.left,
        top: Int = this.padding.top,
        right: Int = this.padding.right,
        bottom: Int = this.padding.bottom
    ): UiModifier =
        this.copy(padding = UiInsets(left, top, right, bottom))

    fun padding(horizontal: Int = this.padding.horizontal, vertical: Int = this.padding.vertical): UiModifier =
        this.copy(padding = UiInsets(horizontal, vertical))

    fun padding(all: Int): UiModifier =
        this.copy(padding = UiInsets(all))

    fun overridePosition(x: Int? = null, y: Int? = null): UiModifier {
        val current = this.overridePosition
        val newX = x ?: current?.x ?: return this
        val newY = y ?: current?.y ?: return this

        return copy(overridePosition = Vector2i(newX, newY))
    }

    protected fun stretchSize(
        width: IUiStretch = this.stretchSize.width,
        height: IUiStretch = this.stretchSize.height
    ): UiModifier =
        this.copy(stretchSize = UiStretchSize(width, height))

    fun width(stretch: IUiStretch): UiModifier =
        this.stretchSize(width = stretch)

    fun height(stretch: IUiStretch): UiModifier =
        this.stretchSize(height = stretch)

    protected fun aligns(h: UiAlign = this.aligns.horizontal, v: UiAlign = this.aligns.vertical): UiModifier =
        this.copy(aligns = UiAligns(h, v))

    fun align(horizontal: UiAlign, vertical: UiAlign): UiModifier =
        this.aligns(horizontal, vertical)

    fun hAlign(horizontal: UiAlign): UiModifier =
        this.aligns(h = horizontal)

    fun vAlign(vertical: UiAlign): UiModifier =
        this.aligns(v = vertical)

    fun transform(block: UiTransform.Builder.() -> Unit): UiModifier {
        val newTransform = UiTransform.Builder().apply(block).build()
        return this.copy(transform = newTransform)
    }

    fun mouseEvents(block: UiMouseEvents.Builder.() -> Unit): UiModifier {
        val newMouseEvents = UiMouseEvents.Builder().apply(block).build()
        return this.copy(mouseEvents = newMouseEvents)
    }

    fun keyEvents(block: UiKeyEvents.Builder.() -> Unit): UiModifier {
        val newKeyEvents = UiKeyEvents.Builder().apply(block).build()
        return this.copy(keyEvents = newKeyEvents)
    }

    fun nodeEvents(block: UiNodeEvents.Builder.() -> Unit): UiModifier {
        val newNodeEvents = UiNodeEvents.Builder().apply(block).build()
        return this.copy(nodeEvents = newNodeEvents)
    }

    //// Utils ////

    fun resolveWidth(contentWidth: Int, availableWidth: Int): Int {
        val currentAvailable = UiRuntime.currentRuntime?.currentAvailableWidth
        return stretchSize.width.resolve(
            minSize.width.result,
            padding.horizontal,
            currentAvailable,
            contentWidth,
            availableWidth
        )
    }

    fun resolveHeight(contentHeight: Int, availableHeight: Int): Int {
        val currentAvailable = UiRuntime.currentRuntime?.currentAvailableHeight
        return stretchSize.height.resolve(
            minSize.height.result,
            padding.vertical,
            currentAvailable,
            contentHeight,
            availableHeight
        )
    }

    fun resolveSize(
        contentWidth: Int,
        contentHeight: Int,
        availableSize: UiSize
    ): UiSize {
        val resolvedWidth = resolveWidth(contentWidth + padding.horizontal, availableSize.width)
        val resolvedHeight = resolveHeight(contentHeight + padding.vertical, availableSize.height)

        val minW = minSize.width.result
        val maxW = maxOf(minW, maxSize?.width?.result ?: availableSize.width)
        val minH = minSize.height.result
        val maxH = maxOf(minH, maxSize?.height?.result ?: availableSize.height)

        return UiSize(
            resolvedWidth.coerceIn(minW, maxW),
            resolvedHeight.coerceIn(minH, maxH)
        )
    }
}
