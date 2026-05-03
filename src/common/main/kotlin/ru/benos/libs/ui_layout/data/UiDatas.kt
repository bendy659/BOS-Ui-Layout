package ru.benos.libs.ui_layout.data

import net.minecraft.world.phys.Vec2
import org.joml.Matrix3x2f
import org.joml.Vector2f
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

data class UiSize(
    val width: Int,
    val height: Int
) {
    companion object {
        val ZERO: UiSize
            get() = UiSize(0 ,0)
    }
}

data class UiInsets(
    val left  : Int,
    val top   : Int,
    val right : Int,
    val bottom: Int
) {
    companion object {
        val ZERO: UiInsets
            get() = UiInsets(0, 0, 0, 0)
    }

    val horizontal: Int
        get() = left + right
    val vertical  : Int
        get() = top + bottom
}

data class UiRect(
    val x     : Int,
    val y     : Int,
    val width : Int,
    val height: Int
) {
    val right : Int
        get() = x + width
    val bottom: Int
        get() = y + height

    fun shrink(insets: UiInsets): UiRect =
        UiRect(
            x      = x + insets.left,
            y      = y + insets.top,
            width  = (width - insets.horizontal).coerceAtLeast(0),
            height = (height - insets.vertical).coerceAtLeast(0)
        )

    fun contains(pX: Number, pY: Number): Boolean {
        val c0 = pX.toInt() >= x
        val c1 = pX.toInt() <= (x + width)
        val c2 = pY.toInt() >= y
        val c3 = pY.toInt() <= (y + height)

        return c0 && c1 && c2 && c3
    }
}

data class UiTransform(
    var origin  : Vec2,
    var offset  : Vec2,
    var rotation: Float,
    var scale   : Vec2
) {
    companion object {
        val DEFAULT: UiTransform
            get() = UiTransform(
                Vec2(0.5f, 0.5f),
                Vec2(0.0f, 0.0f),
                0.0f,
                Vec2(1.0f, 1.0f)
            )
    }

    fun normalizeMouse(mouseX: Float, mouseY: Float, bounds: UiRect): Pair<Float, Float> {
        val pivotX = bounds.x + bounds.width * origin.x
        val pivotY = bounds.y + bounds.height * origin.y

        val transformMatrix = Matrix3x2f()
            .translate(offset.x, offset.y)
            .rotateAbout(rotation, pivotX, pivotY)
            .scaleAround(scale.x, scale.y, pivotX, pivotY)

        val inverseMatrix = transformMatrix.invert()
        val localPoint = inverseMatrix.transformPosition(Vector2f(mouseX, mouseY))

        return (localPoint.x to localPoint.y)
    }

    fun applyToBounds(bounds: UiRect, bl0: Boolean, bl1: Boolean, bl2: Boolean): UiRect {
        val effectiveOffset   = if (bl0)   offset   else Vec2(0f, 0f)
        val effectiveRotation = if (bl1) rotation else 0f
        val effectiveScale    = if (bl2)    scale    else Vec2(1f, 1f)

        val cosA = abs(cos(effectiveRotation))
        val sinA = abs(sin(effectiveRotation))

        val rotatedWidth  = bounds.width  * cosA + bounds.height * sinA
        val rotatedHeight = bounds.width  * sinA + bounds.height * cosA

        val scaledWidth  = (rotatedWidth  * effectiveScale.x).toInt()
        val scaledHeight = (rotatedHeight * effectiveScale.y).toInt()

        val pivotX = bounds.x + bounds.width  * origin.x
        val pivotY = bounds.y + bounds.height * origin.y

        val newX = (pivotX - scaledWidth  * origin.x + effectiveOffset.x).toInt()
        val newY = (pivotY - scaledHeight * origin.y + effectiveOffset.y).toInt()

        return UiRect(newX, newY, scaledWidth, scaledHeight)
    }
}

data class UiClickRegion(
    val rect      : UiRect,
    val transform : UiTransform,
    val clickEvent: (Int, Int, Int) -> Boolean // key, mouseX, mouseY -> catch click
)

data class UiHoverRegion(
    val rect      : UiRect,
    val transform : UiTransform,
    val hoverEvent: (Int, Int) -> Boolean // mouseX, mouseY -> catch hover
)

data class UiScrollRegion(
    val rect: UiRect,
    val onScroll: (Double) -> Unit
)

data class UiDragRegion(
    val rect: UiRect,
    val onStartDrag: (Int, Double, Double) -> Boolean,
    val onDrag     : (Int, Double, Double) -> Boolean,
    val onEndDrag  : (Int, Double, Double) -> Boolean
)