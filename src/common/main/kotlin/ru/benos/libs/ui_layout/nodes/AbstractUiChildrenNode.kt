package ru.benos.libs.ui_layout.nodes

import ru.benos.libs.ui_layout.UiRuntime
import ru.benos.libs.ui_layout.data.IUiLength
import ru.benos.libs.ui_layout.data.UiAlign
import ru.benos.libs.ui_layout.data.UiRect

abstract class AbstractUiChildrenNode: AbstractUiNode() {
    abstract val children: List<IUiNode>
    abstract val enableScissor: Boolean

    override fun render(runtime: UiRuntime, bounds: UiRect) {
        val transformedBounds = modifier.transform.applyToBounds(bounds, modifier.affectOffset, modifier.affectRotation, modifier.affectScale)
        registerEvents(runtime, bounds)

        renderTransformed(runtime.guiGraphics, bounds) {
            renderBackground(runtime, transformedBounds)

            val inner = transformedBounds.shrink(modifier.padding)
            scissor(runtime, inner, enableScissor) {
                renderChildren(runtime, inner)
            }
        }
    }

    protected open fun renderBackground(runtime: UiRuntime, bounds: UiRect) { /* Implementation */ }

    abstract fun renderChildren(runtime: UiRuntime, bounds: UiRect)

    protected fun calcUiLength(uiLength: IUiLength, inner: Int, current: Int?, measure: Int): Int =
        when (uiLength) {
            is IUiLength.Fill      -> inner
            is IUiLength.Available -> current ?: inner
            is IUiLength.Expand    -> measure
            else -> measure.coerceAtMost(inner)
        }

    protected fun calcAlign(align: UiAlign, inner: Int, size: Int): Int =
        when (align) {
            UiAlign.Start  -> 0
            UiAlign.Center -> (inner - size) / 2
            UiAlign.End    -> inner - size
        }
}