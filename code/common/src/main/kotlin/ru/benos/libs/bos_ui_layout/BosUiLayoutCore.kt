package ru.benos.libs.bos_ui_layout

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import ru.benos.libs.bos_ui_layout.api.UiStretch
import ru.benos.libs.bos_ui_layout.builders.UiBuilder
import ru.benos.libs.bos_ui_layout.datas.UiBoxTheme
import ru.benos.libs.bos_ui_layout.datas.UiModifier
import ru.benos.libs.bos_ui_layout.datas.base.UiRect
import ru.benos.libs.bos_ui_layout.nodes.IUiNode
import ru.benos.libs.bos_ui_layout.nodes.UiBoxNode
import kotlin.reflect.KProperty0

open class BosUiLayoutCore(
    val size: KProperty0<UiRect>
) : IBosUiLayout {
    override val contentBounds: UiRect
        get() = size.get()

    override fun UiBuilder.ui() { /* Implementation */ }

    override var runtime: UiRuntime? = null

    var isDirty: Boolean = true
    var cachedUiTree: IUiNode? = null

    private fun buildUi(block: UiBuilder.() -> Unit): IUiNode =
        UiBoxNode(
            UiBoxTheme.TRANSPARENT,
            true,
            UiBuilder().apply { block() }.build(),
            UiModifier.Companion
                .width(UiStretch.fill())
                .height(UiStretch.fill())
        )

    fun rebuildUi(block: UiBuilder.() -> Unit) {
        if (isDirty) {
            cachedUiTree = buildUi(block)
            isDirty = false
        }
    }

    open fun refresh() {
        isDirty = true
    }

    fun render(p0: GuiGraphics, font: Font, p1: Int, p2: Int, block: UiBuilder.() -> Unit) {
        refresh()
        val frameRuntime = newRuntime(p0, font, p1, p2)

        rebuildUi(block)
        cachedUiTree?.render(frameRuntime, contentBounds)
    }

    // Helpers //

    fun newRuntime(p0: GuiGraphics, font: Font, p1: Int, p2: Int): UiRuntime {
        val currentRuntime = runtime
            ?: UiRuntime(p0, font, p1, p2)

        // Ticking delta time //
        val now = System.nanoTime()
        val lastFrameTime = currentRuntime.lastFrameTimeNanos

        currentRuntime.deltaTime =
            when (lastFrameTime == null) {
                true -> 0.0f
                false -> ((now - lastFrameTime).toFloat() / 1_000_000_000.0f)
            }
        currentRuntime.totalTime += currentRuntime.deltaTime
        currentRuntime.lastFrameTimeNanos = now

        currentRuntime.newFrame(p0, font, p1, p2)

        runtime = currentRuntime

        return currentRuntime
    }
}