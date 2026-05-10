package ru.benos.libs.bos_ui_layout.nodes

import ru.benos.libs.bos_ui_layout.UiDsl
import ru.benos.libs.bos_ui_layout.UiRuntime
import ru.benos.libs.bos_ui_layout.datas.UiBoxTheme
import ru.benos.libs.bos_ui_layout.datas.UiModifier
import ru.benos.libs.bos_ui_layout.datas.base.UiNodeContext
import ru.benos.libs.bos_ui_layout.datas.base.UiRect

@UiDsl
open class UiBoxNode(
    private val boxTheme: UiBoxTheme,

    override val enableScissor: Boolean,
    override val children: List<IUiNode>,
    override val modifier: UiModifier
) : AbstractChildrenUiNode() {
    override fun render(runtime: UiRuntime, bounds: UiRect) {
        registerEvents(runtime, bounds)

        transformative(runtime, bounds) {
            renderBackground(runtime, bounds)

            val inner = bounds.shrink(modifier.padding)
            scissor(runtime, inner) { renderChildren(runtime, inner) }
        }
    }

    protected fun renderBackground(runtime: UiRuntime, bounds: UiRect) {
        val backgroundColor =
            when {
                runtime.isMouseClicked(bounds) -> boxTheme.layersClicked
                runtime.isMouseHovered(bounds) -> boxTheme.layersHovered
                runtime.isMouseReleased(bounds) -> boxTheme.layersReleased
                //isFocused       -> boxTheme.backgroundFocused

                else -> boxTheme.layersNormal
            }

        // Background //
        val nodeCtx = UiNodeContext(runtime, bounds)

        backgroundColor.toList().foldRight({ }) { canvas, next ->
            { canvas.render(runtime.guiGraphics, bounds, nodeCtx, next) }
        }.invoke()
    }
}