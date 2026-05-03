package ru.benos.libs.ui_layout.nodes

import net.minecraft.client.gui.GuiGraphics
import ru.benos.libs.ui_layout.UiRuntime
import ru.benos.libs.ui_layout.data.UiModifier
import ru.benos.libs.ui_layout.data.UiRect

abstract class AbstractUiNode: IUiNode {
    abstract override val modifier: UiModifier

    override fun render(runtime: UiRuntime, bounds: UiRect) {
        registerEvents(runtime, bounds)
    }

    protected fun registerEvents(runtime: UiRuntime, bounds: UiRect) {
        val transformedBounds = modifier.transform.applyToBounds(bounds, modifier.affectOffset, modifier.affectRotation, modifier.affectScale) // добавить

        modifier.onMouseClicked?.let { runtime.addMouseClicked(transformedBounds, modifier.transform, it) }

        if (modifier.onMouseEnter != null || modifier.onMouseExit != null || modifier.onMouseHovered != null) {
            val (localX, localY) = modifier.transform
                .normalizeMouse(runtime.mouseX.toFloat(), runtime.mouseY.toFloat(), transformedBounds)

            val isHovered = runtime.trackHover(transformedBounds, localX, localY)
            val wasHovered = runtime.isHovered(transformedBounds)

            if (isHovered && !wasHovered) modifier.onMouseEnter?.invoke()
            if (!isHovered && wasHovered) modifier.onMouseExit?.invoke()
            if (isHovered) modifier.onMouseHovered?.invoke(localX.toInt(), localY.toInt())
        }

        modifier.onMouseReleased?.let { runtime.addMouseReleased(transformedBounds, modifier.transform, it) }
    }

    protected fun renderTransformed(guiGraphics: GuiGraphics, bounds: UiRect, block: () -> Unit) =
        renderTransformedNode(guiGraphics, bounds, modifier, block)

    protected fun scissor(runtime: UiRuntime, inner: UiRect, enableScissor: Boolean, block: () -> Unit) {
        if (enableScissor)
            runtime.guiGraphics.enableScissor(inner.x, inner.y, inner.right, inner.bottom)

        block()

        if (enableScissor)
            runtime.guiGraphics.disableScissor()
    }
}

expect fun renderTransformedNode(guiGraphics: GuiGraphics, bounds: UiRect, modifier: UiModifier, block: () -> Unit)