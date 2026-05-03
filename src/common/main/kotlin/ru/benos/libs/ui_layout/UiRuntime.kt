package ru.benos.libs.ui_layout

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import ru.benos.libs.ui_layout.data.*

class UiRuntime {
    var guiGraphics: GuiGraphics
    var font       : Font
    var mouseX     : Int = 0
    var mouseY     : Int = 0

    constructor(guiGraphics: GuiGraphics, font: Font, mouseX: Int, mouseY: Int) {
        this.guiGraphics = guiGraphics
        this.font        = font
        this.mouseX      = mouseX
        this.mouseY      = mouseY
    }

    companion object {
        internal var currentRuntime: UiRuntime? = null
    }

    // States //
    private val availableSpaceStack   : MutableSet<UiSize>             = mutableSetOf()
    private val overlayRenderers      : MutableSet<(Int, Int) -> Unit> = mutableSetOf()

    private val mouseClickRegions     : MutableSet<UiClickRegion> = mutableSetOf()
    private val mouseReleaseRegions   : MutableSet<UiClickRegion> = mutableSetOf()

    private val scrollRegions   : MutableList<UiScrollRegion> = mutableListOf()
    private val dragRegions     : MutableList<UiDragRegion> = mutableListOf()
    private var activeDragRegion: UiDragRegion? = null

    private val mouseClickedRects     : MutableSet<UiRect> = mutableSetOf()

    private val mouseHoveredRects     : MutableSet<UiRect> = mutableSetOf()
    private val mouseHoveredRectsNext : MutableSet<UiRect> = mutableSetOf()

    private val mouseReleasedRects    : MutableSet<UiRect> = mutableSetOf()
    private val mouseReleasedRectsNext: MutableSet<UiRect> = mutableSetOf()

    var deltaTime: Float = 0.0f
    var totalTime: Float = 0.0f
    var lastFrameTimeNanos: Long? = null

    fun newFrame(guiGraphics: GuiGraphics, font: Font, mouseX: Int, mouseY: Int) {
        // Cleanup //
        availableSpaceStack.clear()
        overlayRenderers.clear()

        mouseClickRegions.clear()
        mouseReleaseRegions.clear()

        scrollRegions.clear()
        dragRegions.clear()

        mouseHoveredRects.clear()
        mouseHoveredRects += mouseHoveredRectsNext
        mouseHoveredRectsNext.clear()

        mouseReleasedRects.clear()
        mouseReleasedRects += mouseReleasedRectsNext
        mouseReleasedRectsNext.clear()

        // Update datas //
        this.guiGraphics = guiGraphics
        this.font = font
        this.mouseX = mouseX; this.mouseY = mouseY

        // Set now UiRuntime //
        currentRuntime = this
    }

    // Regions utils //
    fun addMouseClicked(rect: UiRect, transform: UiTransform, onClicked: (Int, Int, Int) -> Boolean) =
        (mouseClickRegions.add(UiClickRegion(rect, transform, onClicked)))

    fun addMouseReleased(rect: UiRect, transform: UiTransform, onReleased: (Int, Int, Int) -> Boolean) =
        mouseReleaseRegions.add(UiClickRegion(rect, transform, onReleased))

    fun addScrollRegion(rect: UiRect, onScroll: (Double) -> Unit) =
        scrollRegions.add(UiScrollRegion(rect, onScroll))

    fun addDragRegion(
        rect: UiRect,
        onStartDrag: (Int, Double, Double) -> Boolean,
        onDrag: (Int, Double, Double) -> Boolean,
        onEndDrag: (Int, Double, Double) -> Boolean
    ) =
        dragRegions.add(UiDragRegion(rect, onStartDrag, onDrag, onEndDrag))

    // Actions utils //
    fun clicked(key: Int, mouseX: Number, mouseY: Number): Boolean =
        click(key, mouseX.toInt(), mouseY.toInt(), mouseClickRegions, mouseClickedRects)

    fun released(key: Int, mouseX: Number, mouseY: Number): Boolean {
        mouseClickedRects.clear()
        return click(key, mouseX.toInt(), mouseY.toInt(), mouseReleaseRegions, mouseReleasedRectsNext)
    }

    fun scrolled(delta: Double): Boolean {
        val region = scrollRegions.lastOrNull { it.rect.contains(mouseX.toDouble(), mouseY.toDouble()) } ?: return false
        region.onScroll(delta)
        return true
    }

    fun startDrag(key: Int, mouseX: Double, mouseY: Double): Boolean {
        val region = dragRegions.lastOrNull { it.rect.contains(mouseX, mouseY) } ?: return false
        activeDragRegion = region
        region.onStartDrag(key, mouseX, mouseY)
        region.onDrag(key, mouseX, mouseY)
        return true
    }

    fun drag(key: Int, mouseX: Double, mouseY: Double): Boolean {
        activeDragRegion?.onDrag(key, mouseX, mouseY) ?: return false
        return true
    }

    fun endDrag(key: Int, mouseX: Double, mouseY: Double) {
        activeDragRegion?.onEndDrag(key, mouseX, mouseY)
        activeDragRegion = null
    }

    private fun click(key: Int, mouseX: Int, mouseY: Int, regions: Set<UiClickRegion>, rects: MutableSet<UiRect>): Boolean {
        for (region in regions.reversed()) {
            val (localX, localY) = region.transform.normalizeMouse(mouseX.toFloat(), mouseY.toFloat(), region.rect)
            if (!region.rect.contains(localX.toDouble(), localY.toDouble()))
                continue

            if (region.clickEvent(key, localX.toInt(), localY.toInt())) {
                rects += region.rect
                return true
            }
        }
        return false
    }

    fun isHovered(rect: UiRect): Boolean =
        mouseHoveredRects.contains(rect)

    fun isClicked(rect: UiRect): Boolean =
        mouseClickedRects.contains(rect)

    fun isReleased(rect: UiRect): Boolean =
        mouseReleasedRects.contains(rect)

    fun trackHover(rect: UiRect, mouseX: Float, mouseY: Float): Boolean {
        val hovered = rect.contains(mouseX.toDouble(), mouseY.toDouble())
        if (hovered)
            mouseHoveredRectsNext += rect

        return hovered
    }

    // Other utils //
    val currentAvailableWidth : Int?
        get() = availableSpaceStack.lastOrNull()?.width
    val currentAvailableHeight: Int?
        get() = availableSpaceStack.lastOrNull()?.height
}