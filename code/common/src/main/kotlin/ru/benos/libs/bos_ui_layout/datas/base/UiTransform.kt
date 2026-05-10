package ru.benos.libs.bos_ui_layout.datas.base

import org.joml.*

data class UiTransform(
    val origin  : Vector3f,
    val offset  : Vector3f,
    val rotation: Vector3f,
    val scale   : Vector2f,

    val afterOffset  : Boolean,
    val afterRotation: Boolean,
    val afterScale   : Boolean
) {
    class Builder {
        var origin: Vector3f = Vector3f(0.0f, 0.0f, 0.0f)
        var offset: Vector3f = Vector3f(0.0f, 0.0f, 0.0f)
        var rotation: Vector3f = Vector3f(0.0f, 0.0f, 0.0f)
        var scale: Vector2f = Vector2f(1.0f, 1.0f)

        var afterOffset: Boolean = false
        var afterRotation: Boolean = false
        var afterScale: Boolean = false

        fun build(): UiTransform =
            UiTransform(
                this.origin,
                this.offset, this.rotation, this.scale,
                this.afterOffset, this.afterRotation, this.afterScale
            )

        fun origin(x: Float, y: Float, z: Float) {
            this.origin = Vector3f(x, y, z)
        }

        fun offset(x: Float, y: Float, z: Float) {
            this.offset = Vector3f(x, y, z)
        }

        fun rotation(x: Float, y: Float, z: Float) {
            this.rotation = Vector3f(x, y, z)
        }
        fun rotationDegrees(x: Float, y: Float, z: Float) {
            this.rotation = Vector3f(
                Math.toRadians(x.toDouble()).toFloat(),
                Math.toRadians(y.toDouble()).toFloat(),
                Math.toRadians(z.toDouble()).toFloat()
            )
        }

        fun scale(x: Float, y: Float) {
            this.scale = Vector2f(x, y)
        }

        fun afterLayout(
            offset: Boolean = false,
            rotation: Boolean = false,
            scale: Boolean = false
        ) {
            this.afterOffset = offset
            this.afterRotation = rotation
            this.afterScale = scale
        }
    }

    companion object {
        val DEFAULT: UiTransform
            get() = UiTransform(
                origin = Vector3f(0.0f, 0.0f, 0.0f),
                offset   = Vector3f(0.0f, 0.0f, 0.0f),
                rotation = Vector3f(0.0f, 0.0f, 0.0f),
                scale    = Vector2f(1.0f, 1.0f),
                afterOffset   = false,
                afterRotation = false,
                afterScale    = false
            )
    }

    fun normalizeMouse(mouse: Vector2i, bounds: UiRect): Pair<Float, Float> {
        val pivotX = bounds.x + bounds.width * origin.x
        val pivotY = bounds.y + bounds.height * origin.y

        val matrix = Matrix4f()
            .translate(pivotX, pivotY, 0.0f)
            .translate(offset.x.toFloat(), offset.y.toFloat(), 0.0f)
            .rotateX(rotation.x)
            .rotateY(rotation.y)
            .rotateZ(rotation.z)
            .scale(scale.x, scale.y, 1.0f)
            .translate(-pivotX, -pivotY, 0.0f)

        matrix.invert()

        val local = Vector4f(mouse.x.toFloat(), mouse.y.toFloat(), 0.0f, 1.0f)
            .mul(matrix)

        val normalizedX = (local.x - bounds.x) / bounds.width
        val normalizedY = (local.y - bounds.y) / bounds.height

        return normalizedX to normalizedY
    }

    fun recalcSize(size: UiSize): UiSize {
        var width = size.width.toFloat()
        var height = size.height.toFloat()

        if (afterScale) {
            width *= scale.x
            height *= scale.y
        }

        if (afterRotation) {
            // Пересчёт AABB после вращения
            val rad = rotation.z
            val cos = kotlin.math.abs(kotlin.math.cos(rad))
            val sin = kotlin.math.abs(kotlin.math.sin(rad))
            val newWidth = width * cos + height * sin
            val newHeight = width * sin + height * cos
            width = newWidth
            height = newHeight
        }

        if (afterOffset) {
            width += kotlin.math.abs(offset.x)
            height += kotlin.math.abs(offset.y)
        }

        return UiSize(width.toInt(), height.toInt())
    }
}
