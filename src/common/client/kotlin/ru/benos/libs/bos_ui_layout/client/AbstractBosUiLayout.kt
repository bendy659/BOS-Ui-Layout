package ru.benos.libs.bos_ui_layout.client

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import ru.benos.libs.bos_ui_layout.client.builders.UiBuilder
import ru.benos.libs.bos_ui_layout.client.datas.UiRect
import ru.benos.libs.bos_ui_layout.client.nodes.IUiNode

abstract class AbstractBosUiLayout: Screen(Component.empty()), IBosUiLayout {
    override val contentBounds: UiRect
        get() = UiRect.stretch(width, height)

    override var runtime: UiRuntime? = null

    private var isDirty: Boolean = true
    private var cachedUiTree: IUiNode? = null

    abstract override fun UiBuilder.ui()

    protected fun buildUi(): IUiNode = TODO()

    protected fun rebuildUi() {
        if (isDirty) {
            cachedUiTree = buildUi()
            isDirty = false
        }
    }

    open fun refresh() { isDirty = true }

    // Overrides //

    override fun render(p0: GuiGraphics, p1: Int, p2: Int, p3: Float) {
        refresh()
        val frameRuntime = newRuntime(p0, p1, p2)


        this.rebuildUi()
        cachedUiTree?.render(frameRuntime, contentBounds)
    }

    override fun mouseClicked(p0: Double, p1: Double, p2: Int): Boolean {
        val mouseClicked = runtime?.mouseClicked(p2, p0, p1)
        if (mouseClicked == true)
            return true

        return super.mouseClicked(p0, p1, p2)
    }

    override fun mouseReleased(p0: Double, p1: Double, p2: Int): Boolean {
        val mouseReleased = runtime?.mouseReleased(p2, p0, p1)
        if (mouseReleased == true)
            return true

        return super.mouseReleased(p0, p1, p2)
    }

    override fun mouseDragged(p0: Double, p1: Double, p2: Int, p3: Double, p4: Double): Boolean {
        val mouseDragged = runtime?.mouseDragged(p2, p3, p4)
        if (mouseDragged == true)
            return true

        return super.mouseDragged(p0, p1, p2, p3, p4)
    }

    override fun mouseScrolled(p0: Double, p1: Double, p2: Double, p3: Double): Boolean {
        val mouseScrolled = runtime?.mouseScrolled(p0, p1, p2, p3)
        if (mouseScrolled == true)
            return true

        return super.mouseScrolled(p0, p1, p2, p3)
    }

    override fun keyPressed(p0: Int, p1: Int, p2: Int): Boolean {
        return super.keyPressed(p0, p1, p2)
    }

    override fun keyReleased(p0: Int, p1: Int, p2: Int): Boolean {
        return super.keyReleased(p0, p1, p2)
    }

    // Helpers //

    protected fun newRuntime(p0: GuiGraphics, p1: Int, p2: Int): UiRuntime {
        val currentRuntime = runtime
            ?: UiRuntime(p0, font, p1, p2)

        // Ticking delta time //
        val now = System.nanoTime()
        val lastFrameTime = currentRuntime.lastFrameTimeNanos

        currentRuntime.deltaTime =
            when (lastFrameTime == null) {
                true  -> 0.0f
                false -> ((now - lastFrameTime) / 1_000_000_000).toFloat()
            }
        currentRuntime.totalTime += currentRuntime.deltaTime
        currentRuntime.lastFrameTimeNanos = now

        currentRuntime.newFrame(p0, font, p1, p2)

        runtime = currentRuntime

        return currentRuntime
    }
}