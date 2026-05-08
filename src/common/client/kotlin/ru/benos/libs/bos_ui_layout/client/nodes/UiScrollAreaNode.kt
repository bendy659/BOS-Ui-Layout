package ru.benos.libs.bos_ui_layout.client.nodes

import net.minecraft.client.gui.GuiGraphics
import ru.benos.libs.bos_ui_layout.client.UiDsl
import ru.benos.libs.bos_ui_layout.client.UiRuntime
import ru.benos.libs.bos_ui_layout.client.datas.UIScrollState
import ru.benos.libs.bos_ui_layout.client.datas.UiModifier
import ru.benos.libs.bos_ui_layout.client.datas.UiRect
import ru.benos.libs.bos_ui_layout.client.datas.UiSize

@UiDsl
class UiScrollAreaNode(
    private val state: UIScrollState,
    private val hScrollable: Boolean,
    private val vScrollable: Boolean,

    override val children: List<IUiNode>,
    override val modifier: UiModifier
): AbstractChildrenUiNode() {
    override val enableScissor: Boolean = true

    override fun measure(runtime: UiRuntime, availableSize: UiSize): UiSize {
        val inner = UiRect(0, 0, availableSize).shrink(modifier.padding)

        val childAvailable = UiSize(
            if (hScrollable) Int.MAX_VALUE / 2 else inner.width,
            if (vScrollable)   Int.MAX_VALUE / 2 else inner.height
        )

        children.forEach { child ->
            val measured = child.measure(runtime, childAvailable)
            state.contentSize.maxOf(measured)
        }

        return modifier.resolveSize(inner.width, inner.height, availableSize)
    }

    override fun render(runtime: UiRuntime, bounds: UiRect) {
        super.render(runtime, bounds)

        val inner = bounds.shrink(modifier.padding)
        val maxScrollX = (state.contentSize.width - inner.width).coerceAtLeast(0)
        val maxScrollY = (state.contentSize.height - inner.height).coerceAtLeast(0)

        state.scrollX = state.scrollX.coerceIn(0, maxScrollX)
        state.scrollY = state.scrollY.coerceIn(0, maxScrollY)

        scissor(runtime, inner) {
            scrollOffsetContent(runtime.guiGraphics) {
                children.forEach { child ->
                    child.render(runtime, UiRect(inner, state.contentSize))
                }
            }
        }
    }

    private fun scrollOffsetContent(guiGraphics: GuiGraphics, block: () -> Unit) {
        val pose = guiGraphics.pose()
        pose.pushPose()

        pose.translate(-state.scrollX.toFloat(), -state.scrollY.toFloat(), 0.0f)
        block()

        pose.popPose()
    }
}