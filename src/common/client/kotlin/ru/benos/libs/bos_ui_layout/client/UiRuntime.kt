package ru.benos.libs.bos_ui_layout.client

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import org.joml.Vector2i
import ru.benos.libs.bos_ui_layout.client.datas.UiRect
import ru.benos.libs.bos_ui_layout.client.datas.UiSize
import ru.benos.libs.bos_ui_layout.client.datas.UiTransform
import ru.benos.libs.bos_ui_layout.client.datas.events.UiEventRegions3

class UiRuntime(
    var guiGraphics: GuiGraphics,
    var font       : Font,
    var mouse      : Vector2i
) {
    constructor(guiGraphics: GuiGraphics, font: Font, mouseX: Int, mouseY: Int):
            this(guiGraphics, font, Vector2i(mouseX, mouseY))

    companion object {
        var currentRuntime: UiRuntime? = null
    }

    //// States ////

    private val availableSpaceStack: MutableList<UiSize> = mutableListOf()

    private val mouseClickRegions  : MutableList<UiEventRegions3<Int, Int, Int, Boolean>> = mutableListOf()
    private val mouseReleaseRegions: MutableList<UiEventRegions3<Int, Int, Int, Boolean>> = mutableListOf()
    private val mouseScrollRegions : MutableList<UiEventRegions3<Boolean, Double, Double, Boolean>> = mutableListOf()
    private val mouseDragRegions   : MutableList<UiEventRegions3<Int, Double, Double, Boolean>> = mutableListOf()

    private val mouseClickedRects : MutableSet<UiRect> = mutableSetOf()
    private val mouseHoveredRects    : MutableSet<UiRect> = mutableSetOf()
    private val mouseHoveredRectsNext: MutableSet<UiRect> = mutableSetOf()
    private val mouseReleasedRects: MutableSet<UiRect> = mutableSetOf()
    private val mouseReleasedRectsNext: MutableSet<UiRect> = mutableSetOf()

    var deltaTime: Float = 0.0f
    var totalTime: Float = 0.0f
    var lastFrameTimeNanos: Long? = null

    fun newFrame(guiGraphics: GuiGraphics, font: Font, mouseX: Int, mouseY: Int) {
        // Cleanup //
        availableSpaceStack.clear()

        mouseClickRegions.clear()
        mouseReleaseRegions.clear()

        mouseHoveredRects.clear()
        mouseHoveredRects += mouseHoveredRectsNext
        mouseHoveredRectsNext.clear()

        mouseReleasedRects.clear()
        mouseReleasedRects += mouseReleasedRectsNext
        mouseReleasedRectsNext.clear()

        // Update datas //
        this.guiGraphics = guiGraphics
        this.font = font
        this.mouse = Vector2i(mouseX, mouseY)

        // Apply //
        currentRuntime = this
    }

    //// Mouse events ///

    fun addMouseClickRegion(rect: UiRect, transform: UiTransform, event: (Int, Int, Int) -> Boolean) =
        mouseClickRegions.add(UiEventRegions3(rect, transform, event))

    fun addMouseReleaseRegion(rect: UiRect, transform: UiTransform, event: (Int, Int, Int) -> Boolean) =
        mouseReleaseRegions.add(UiEventRegions3(rect, transform, event))

    fun addScrollRegion(rect: UiRect, transform: UiTransform, event: (Boolean, Double, Double) -> Boolean) =
        mouseScrollRegions.add(UiEventRegions3(rect, transform, event))

    fun addDragRegion(rect: UiRect, transform: UiTransform, event: (Int, Double, Double) -> Boolean) =
        mouseDragRegions.add(UiEventRegions3(rect, transform, event))

    //// Action utils ////

    private fun click(key: Int, mouseX: Int, mouseY: Int, regions: List<UiEventRegions3<Int, Int, Int, Boolean>>, rects: MutableSet<UiRect>): Boolean {
        for (region in regions.reversed()) {
            val (localX, localY) = region.transform.normalizeMouse(Vector2i(mouseX, mouseY), region.rect)
            if (!region.rect.contains(localX.toDouble(), localY.toDouble()))
                continue

            if (region.event(key, localX.toInt(), localY.toInt())) {
                rects += region.rect
                return true
            }
        }
        return false
    }

    fun mouseClicked(key: Int, mouseX: Number, mouseY: Number): Boolean =
        click(key, mouseX.toInt(), mouseY.toInt(), mouseClickRegions, mouseClickedRects)

    fun mouseReleased(key: Int, mouseX: Number, mouseY: Number): Boolean {
        mouseClickedRects.clear()
        return click(key, mouseX.toInt(), mouseY.toInt(), mouseReleaseRegions, mouseReleasedRectsNext)
    }

    fun mouseScrolled(mouseX: Number, mouseY: Number, scrollX: Double, scrollY: Double): Boolean {
        for (region in mouseScrollRegions.reversed()) {
            val (localX, localY) = region.transform.normalizeMouse(Vector2i(mouseX.toInt(), mouseY.toInt()), region.rect)
            if (!region.rect.contains(localX.toDouble(), localY.toDouble()))
                continue

            if (region.event(true, scrollX, scrollY)) {
                return true
            }
        }
        return false
    }

    fun mouseDragged(mouseX: Number, mouseY: Number, button: Int, dragX: Double, dragY: Double): Boolean {
        for (region in mouseDragRegions.reversed()) {
            val (localX, localY) = region.transform.normalizeMouse(Vector2i(mouseX.toInt(), mouseY.toInt()), region.rect)
            if (!region.rect.contains(localX.toDouble(), localY.toDouble()))
                continue

            if (region.event(button, dragX, dragY)) {
                return true
            }
        }
        return false
    }

    fun isMouseClicked(rect: UiRect): Boolean =
        mouseClickedRects.contains(rect)

    fun isMouseHovered(rect: UiRect): Boolean =
        mouseClickRegions.any { it.rect == rect } ||
                mouseReleaseRegions.any { it.rect == rect }

    fun isMouseReleased(rect: UiRect): Boolean =
        mouseReleasedRects.contains(rect)

    fun isMouseScrolled(rect: UiRect): Boolean =
        mouseReleaseRegions.any { it.rect == rect }

    fun isMouseDragged(rect: UiRect): Boolean =
        mouseDragRegions.any { it.rect == rect }

    fun trackHover(rect: UiRect, localX: Int, localY: Int): Boolean {
        val hovered = rect.contains(localX, localY)
        if (hovered)
            mouseClickRegions.firstOrNull { it.rect == rect }
                ?.let { mouseHoveredRectsNext += it.rect }
        return hovered
    }

    //// Utils ////

    val currentAvailableWidth: Int?
        get() = availableSpaceStack.lastOrNull()?.width
    val currentAvailableHeight: Int?
        get() = availableSpaceStack.lastOrNull()?.height
}