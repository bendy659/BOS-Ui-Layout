package ru.benos.libs.bos_ui_layout.client.nodes

import ru.benos.libs.bos_ui_layout.client.UiRuntime
import ru.benos.libs.bos_ui_layout.client.datas.UiRect
import ru.benos.libs.bos_ui_layout.client.datas.UiSize

abstract class AbstractUiNode: IUiNode {
    protected open val enableScissor: Boolean = false

    override fun measure(runtime: UiRuntime, availableSize: UiSize): UiSize =
        availableSize

    override fun render(runtime: UiRuntime, bounds: UiRect) {
        registerEvents(runtime, bounds)
    }

    protected fun registerEvents(runtime: UiRuntime, bounds: UiRect) {
        modifier.mouseEvents.onClicked?.let { runtime.addMouseClickRegion(bounds, modifier.transform, it) }

        val c0 = modifier.mouseEvents.onEntered != null
        val c1 = modifier.mouseEvents.onExisted != null
        val c2 = modifier.mouseEvents.onHovered != null
        if (c0 || c1 || c2) {
            val (localX, localY) = modifier.transform
                .normalizeMouse(runtime.mouse, bounds)

            val isHovered = runtime.trackHover(bounds, localX.toInt(), localY.toInt())
            val wasHovered = runtime.isMouseHovered(bounds)

            if (isHovered && !wasHovered)
                modifier.mouseEvents.onEntered?.invoke()
            if (!isHovered && wasHovered)
                modifier.mouseEvents.onExisted?.invoke()
            if (isHovered)
                modifier.mouseEvents.onHovered?.invoke(localX.toInt(), localY.toInt())
        }
    }

    protected open fun scissor(runtime: UiRuntime, bounds: UiRect, block: () -> Unit) {
        if (enableScissor)
            runtime.guiGraphics.enableScissor(bounds.x, bounds.y, bounds.right, bounds.bottom)

        block()

        if (enableScissor)
            runtime.guiGraphics.disableScissor()
    }
}