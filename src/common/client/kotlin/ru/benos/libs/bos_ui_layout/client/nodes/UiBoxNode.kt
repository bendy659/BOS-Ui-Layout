package ru.benos.libs.bos_ui_layout.client.nodes

import ru.benos.libs.bos_ui_layout.client.UiDsl
import ru.benos.libs.bos_ui_layout.client.UiRuntime
import ru.benos.libs.bos_ui_layout.client.datas.UiBoxTheme
import ru.benos.libs.bos_ui_layout.client.datas.UiModifier
import ru.benos.libs.bos_ui_layout.client.datas.UiRect

@UiDsl
open class UiBoxNode(
    private val boxTheme: UiBoxTheme,

    override val enableScissor: Boolean,
    override val children: List<IUiNode>,
    override val modifier: UiModifier
): AbstractChildrenUiNode() {
    override fun render(runtime: UiRuntime, bounds: UiRect) {
        registerEvents(runtime, bounds)

        renderBackground(runtime, bounds)
        transformative(runtime, bounds) {
            val inner = bounds.shrink(modifier.padding)
            scissor(runtime, inner) { renderChildren(runtime, inner) }
        }
    }

    protected fun renderBackground(runtime: UiRuntime, bounds: UiRect) {
        val backgroundColor =
            when {
                runtime.isMouseClicked(bounds)  -> boxTheme.layersClicked
                runtime.isMouseHovered(bounds)  -> boxTheme.layersHovered
                runtime.isMouseReleased(bounds) -> boxTheme.layersReleased
                //isFocused       -> boxTheme.backgroundFocused

                else -> boxTheme.layersNormal
            }

        // Background //
        backgroundColor.forEach { it.render(runtime.guiGraphics, bounds) }
    }
}