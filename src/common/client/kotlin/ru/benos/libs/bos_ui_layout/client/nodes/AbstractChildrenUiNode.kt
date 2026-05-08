package ru.benos.libs.bos_ui_layout.client.nodes

import org.joml.Quaternionf
import ru.benos.libs.bos_ui_layout.client.UiRuntime
import ru.benos.libs.bos_ui_layout.client.datas.UiRect
import ru.benos.libs.bos_ui_layout.client.datas.UiSize
import ru.benos.libs.bos_ui_layout.client.datas.UiTransform
import kotlin.math.max

abstract class AbstractChildrenUiNode: AbstractUiNode() {
    abstract val enableScissor: Boolean

    abstract val children: List<IUiNode>

    override fun measure(runtime: UiRuntime, availableSize: UiSize): UiSize {
        val inner = UiRect(0, 0, availableSize).shrink(modifier.padding)

        var contentWidth = 0
        var contentHeight = 0

        children.forEach { child ->
            val measured = child.measure(runtime, UiSize(inner.width, inner.height))
            contentWidth  = max(contentWidth, measured.width)
            contentHeight = max(contentHeight, measured.height)
        }

        return modifier.resolveSize(contentWidth, contentHeight, availableSize)
    }

    override fun render(runtime: UiRuntime, bounds: UiRect) {
        super.render(runtime, bounds)

        transformative(runtime, bounds) {
            val inner = bounds.shrink(modifier.padding)
            scissor(runtime, inner) { renderChildren(runtime, inner) }
        }
    }

    protected open fun renderChildren(runtime: UiRuntime, bounds: UiRect) {
        children.forEach { child ->
            val measured = child.measure(runtime, bounds.size)

            val childSize = UiSize(
                modifier.stretchSize.width.calcLength(bounds.width, runtime.currentAvailableWidth, measured.width),
                modifier.stretchSize.height.calcLength(bounds.height, runtime.currentAvailableHeight, measured.height)
            )
            val (hAlignOffset, vAlignOffset) = modifier.aligns.calcAligns(bounds.size, childSize)

            val childBounds = UiRect(bounds.x + hAlignOffset, bounds.y + vAlignOffset, childSize)
            child.render(runtime, childBounds)
        }
    }

    protected open fun transformative(runtime: UiRuntime, bounds: UiRect, block: () -> Unit) {
        val hasTransform = modifier.transform != UiTransform.DEFAULT
        if (hasTransform) {
            val pose = runtime.guiGraphics.pose()
            pose.pushPose()

            val pivotX = bounds.x + bounds.width  * modifier.transform.origin.x
            val pivotY = bounds.y + bounds.height * modifier.transform.origin.y

            val offset   = modifier.transform.offset
            val rotation = modifier.transform.rotation
            val scale    = modifier.transform.scale

            // 1. Идём в точку пивота
            pose.translate(pivotX, pivotY, 0f)

            // 2. Применяем offset
            pose.translate(offset.x.toFloat(), offset.y.toFloat(), 0f)

            // 3. Крутим вокруг пивота
            pose.mulPose(
                Quaternionf()
                    .rotateX(rotation.x)
                    .rotateY(rotation.y)
                    .rotateZ(rotation.z)
            )

            // 4. Скейлим вокруг пивота
            pose.scale(scale.x, scale.y, 1f)

            // 5. Возвращаемся обратно из пивота
            pose.translate(-pivotX, -pivotY, 0f)
        }

        block()

        if (hasTransform)
            runtime.guiGraphics.pose().popPose()
    }
}