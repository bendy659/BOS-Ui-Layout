package ru.benos.libs.bos_ui_layout.nodes

import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector4f
import ru.benos.libs.bos_ui_layout.UiRuntime
import ru.benos.libs.bos_ui_layout.datas.base.UiRect
import ru.benos.libs.bos_ui_layout.datas.base.UiSize
import ru.benos.libs.bos_ui_layout.datas.base.UiTransform

abstract class AbstractUiNode : IUiNode {
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

    protected open fun transformative(runtime: UiRuntime, bounds: UiRect, block: () -> Unit) {
        val hasTransform = modifier.transform != UiTransform.DEFAULT
        if (hasTransform) {
            val pose = runtime.guiGraphics.pose()
            pose.pushPose()

            val pivotX = bounds.x + bounds.width * modifier.transform.origin.x
            val pivotY = bounds.y + bounds.height * modifier.transform.origin.y
            val pivotZ = modifier.transform.origin.z

            val offset = modifier.transform.offset
            val rotation = modifier.transform.rotation
            val scale = modifier.transform.scale

            val q = Quaternionf()
                .rotateX(rotation.x)
                .rotateY(rotation.y)
                .rotateZ(rotation.z)

            val normal = Vector4f(0f, 0f, 1f, 0f).mul(Matrix4f().rotate(q))
            if (normal.z < 0f) {
                pose.popPose()
                return
            }

            // 1. Идём в точку пивота
            pose.translate(pivotX, pivotY, pivotZ)

            // 2. Применяем offset
            pose.translate(offset.x, offset.y, offset.z)

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
            pose.translate(-pivotX, -pivotY, -pivotZ)
        }

        block()

        if (hasTransform)
            runtime.guiGraphics.pose().popPose()
    }

    protected fun UiSize.applyTransformLayout(): UiSize =
        modifier.transform.recalcSize(this)
}