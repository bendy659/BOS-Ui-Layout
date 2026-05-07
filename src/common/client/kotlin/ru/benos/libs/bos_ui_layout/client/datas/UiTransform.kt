package ru.benos.libs.bos_ui_layout.client.datas

import org.joml.*

data class UiTransform(
    val origin  : Vector2f,
    val offset  : Vector2i,
    val rotation: Vector3f,
    val scale   : Vector2f
) {
    class Builder {
        var origin  : Vector2f = Vector2f(0.0f, 0.0f)
        var offset  : Vector2i = Vector2i(0, 0)
        var rotation: Vector3f = Vector3f(0.0f, 0.0f, 0.0f)
        var scale   : Vector2f = Vector2f(1.0f, 1.0f)

        fun build(): UiTransform =
            UiTransform(this.origin, this.offset, this.rotation, this.scale)

        fun origin(x: Float, y: Float) {
            this.origin = Vector2f(x, y)
        }

        fun offset(x: Int, y: Int) {
            this.offset = Vector2i(x, y)
        }

        fun rotation(x: Float, y: Float, z: Float) {
            this.rotation = Vector3f(x, y, z)
        }

        fun scale(x: Float, y: Float) {
            this.scale = Vector2f(x, y)
        }
    }

    companion object {
        val DEFAULT: UiTransform
            get() = UiTransform(
                Vector2f(0.0f, 0.0f),
                Vector2i(0, 0),
                Vector3f(0.0f, 0.0f, 0.0f),
                Vector2f(1.0f, 1.0f)
            )
    }

    fun normalizeMouse(mouse: Vector2i, bounds: UiRect): Pair<Float, Float> {
        val pivotX = bounds.x + bounds.width  * origin.x
        val pivotY = bounds.y + bounds.height * origin.y

        val matrix = Matrix4f()
            .translate(pivotX, pivotY, 0.0f)
            .rotateX(rotation.x)
            .rotateY(rotation.y)
            .rotateZ(rotation.z)
            .scale(scale.x, scale.y, 1.0f)
            .translate(-pivotX, -pivotY, 0.0f)
            .translate(offset.x.toFloat(), offset.y.toFloat(), 0.0f)

        matrix.invert()

        val local = Vector4f(mouse.x.toFloat(), mouse.y.toFloat(), 0.0f, 1.0f)
            .mul(matrix)

        val normalizedX = (local.x - bounds.x) / bounds.width
        val normalizedY = (local.y - bounds.y) / bounds.height

        return normalizedX to normalizedY
    }
}
