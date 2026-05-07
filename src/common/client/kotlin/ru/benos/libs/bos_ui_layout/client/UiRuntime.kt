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

    private val mouseClickRegions: MutableList<UiEventRegions3<Int, Int, Int, Boolean>> = mutableListOf()
    private val mouseReleaseRegions: MutableList<UiEventRegions3<Int, Int, Int, Boolean>> = mutableListOf()

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

    fun clicked(key: Int, mouseX: Int, mouseY: Int): Boolean =
        click(key, mouseX, mouseY, mouseClickRegions, mouseClickedRects)

    fun released(key: Int, mouseX: Int, mouseY: Int): Boolean {
        mouseClickedRects.clear()
        return click(key, mouseX, mouseY, mouseReleaseRegions, mouseReleasedRectsNext)
    }

    fun isClicked(rect: UiRect): Boolean =
        mouseClickedRects.contains(rect)

    fun isHovered(rect: UiRect): Boolean =
        mouseClickRegions.any { it.rect == rect } ||
                mouseReleaseRegions.any { it.rect == rect }

    fun isReleased(rect: UiRect): Boolean =
        mouseReleasedRects.contains(rect)

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