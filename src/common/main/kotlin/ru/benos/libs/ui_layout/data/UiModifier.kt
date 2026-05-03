package ru.benos.libs.ui_layout.data

import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec2
import org.joml.Vector2i
import ru.benos.libs.ui_layout.UiRuntime

open class UiModifier(
    // Basic //
    var minSize: UiSize = UiSize.ZERO,

    var overridePosition: Vector2i? = null,

    var width : IUiLength = IUiLength.Wrap,
    var height: IUiLength = IUiLength.Wrap,

    var hAlign: UiAlign = UiAlign.Start,
    var vAlign: UiAlign = UiAlign.Start,

    var padding: UiInsets = UiInsets.ZERO,

    var transform     : UiTransform = UiTransform.DEFAULT,
    var affectOffset  : Boolean = false,
    var affectRotation: Boolean = false,
    var affectScale   : Boolean = false,

    //var tooltip: UiBoxNode? = null,

    var zLayer: Int = 0,

    // Events //
    var onMouseEnter  : (()         -> Unit)? = null,
    var onMouseHovered: ((Int, Int) -> Unit)? = null,
    var onMouseExit   : (()         -> Unit)? = null,

    var onMouseClicked : ((Int, Int, Int) -> Boolean)? = null,
    var onMouseReleased: ((Int, Int, Int) -> Boolean)? = null,

    var onMouseDragged : ((Int, Float, Float) -> Boolean)? = null,
    var onMouseScrolled: ((Double)            -> Boolean)? = null,

    var onKeyPressed : ((Int)  -> Boolean)? = null,
    var onKeyReleased: ((Int)  -> Boolean)? = null,
    var onCharTyped  : ((Char) -> Boolean)? = null,

    var onFocused: (() -> Unit)? = null
) {
    companion object: UiModifier()

    //// Builder ////

    /// Basic ///

    fun minSize(minWidth: Int, minHeight: Int): UiModifier {
        this.minSize = UiSize(minWidth, minHeight)
        return this
    }
    fun minSizeWidth(minWidth: Int): UiModifier =
        minSize(minWidth, this.minSize.height)
    fun minSizeHeight(minHeight: Int): UiModifier =
        minSize(this.minSize.width, minHeight)

    // Width //
    fun fixedWidth(value: Int): UiModifier {
        this.width = IUiLength.Fixed(value)
        return this
    }
    fun fillWidth(weight: Float = 1.0f): UiModifier {
        this.width = IUiLength.Fill(weight)
        return this
    }
    fun availableWidth(weight: Float = 1.0f): UiModifier {
        this.width = IUiLength.Available(weight)
        return this
    }
    fun expandWidth(weight: Float = 1.0f): UiModifier {
        this.width = IUiLength.Expand(weight)
        return this
    }

    // Override position //
    fun overridePosition(overrideX: Int, overrideY: Int): UiModifier {
        this.overridePosition = Vector2i(overrideX, overrideY)
        return this
    }

    // Width //
    fun fixedHeight(value: Int): UiModifier {
        this.height = IUiLength.Fixed(value)
        return this
    }
    fun fillHeight(weight: Float = 1.0f): UiModifier {
        this.height = IUiLength.Fill(weight)
        return this
    }
    fun availableHeight(weight: Float = 1.0f): UiModifier {
        this.height = IUiLength.Available(weight)
        return this
    }
    fun expandHeight(weight: Float = 1.0f): UiModifier {
        this.height = IUiLength.Expand(weight)
        return this
    }

    // Align //
    fun align(hAlign: UiAlign, vAlign: UiAlign): UiModifier {
        this.hAlign = hAlign; this.hAlign = hAlign
        return this
    }
    fun hAlign(hAlign: UiAlign): UiModifier =
        align(hAlign, this.vAlign)
    fun vAlign(vAlign: UiAlign): UiModifier =
        align(this.hAlign, vAlign)

    // Padding //
    fun padding(
        left  : Int = this.padding.left,
        top   : Int = this.padding.top,
        right : Int = this.padding.right,
        bottom: Int = this.padding.bottom
    ): UiModifier {
        this.padding = UiInsets(left, top, right, bottom)
        return this
    }
    fun padding(horizontal: Int, vertical  : Int): UiModifier =
        padding(horizontal, vertical, horizontal, vertical)
    fun padding(all: Int): UiModifier =
        padding(all, all, all, all)

    // Transform //
    fun origin(originX: Float, originY: Float): UiModifier {
        this.transform.origin = Vec2(originX, originY)
        return this
    }
    fun offset(offsetX: Float, offsetY: Float, affectOffset: Boolean = this.affectOffset): UiModifier {
        this.transform.offset = Vec2(offsetX, offsetY)
        this.affectOffset = affectOffset
        return this
    }
    fun rotation(radian: Float, affectRotation: Boolean = this.affectRotation): UiModifier {
        this.transform.rotation = radian
        this.affectRotation = affectRotation
        return this
    }
    fun rotationDegrees(degrees: Float, affectRotation: Boolean = this.affectRotation): UiModifier {
        this.transform.rotation = (Mth.PI / 180.0f) * degrees
        this.affectRotation = affectRotation
        return this
    }
    fun scale(scaleX: Float, scaleY: Float, affectScale: Boolean = this.affectScale): UiModifier {
        this.transform.scale = Vec2(scaleX, scaleY)
        this.affectScale = affectScale
        return this
    }

    fun zLayer(index: Int): UiModifier {
        this.zLayer = index
        return this
    }

    /// Events ///

    // Mouse events //
    fun onMouseEnter(block: () -> Unit): UiModifier {
        this.onMouseEnter = block
        return this
    }
    fun onMouseHovered(block: (mouseX: Int, mouseY: Int) -> Unit): UiModifier {
        this.onMouseHovered = block
        return this
    }
    fun onMouseExit(block: () -> Unit): UiModifier {
        this.onMouseExit = block
        return this
    }

    fun onMouseClicked(block: (key: Int, mouseX: Int, mouseY: Int) -> Boolean): UiModifier {
        this.onMouseClicked = block
        return this
    }
    fun onMouseReleased(block: (key: Int, mouseX: Int, mouseY: Int) -> Boolean): UiModifier {
        this.onMouseReleased = block
        return this
    }

    fun onMouseDragged(block: (key: Int, deltaX: Float, deltaY: Float) -> Boolean): UiModifier {
        this.onMouseDragged = block
        return this
    }
    fun onMouseScrolled(block: (factor: Double) -> Boolean): UiModifier {
        this.onMouseScrolled = block
        return this
    }

    fun onKeyPressed(block: (key: Int) -> Boolean): UiModifier {
        this.onKeyPressed = block
        return this
    }
    fun onKeyReleased(block: (key: Int) -> Boolean): UiModifier {
        this.onKeyReleased = block
        return this
    }
    fun onCharTyped(block: (Char) -> Boolean): UiModifier {
        this.onCharTyped = block
        return this
    }

    fun onFocused(block: () -> Unit): UiModifier {
        this.onFocused = block
        return this
    }

    //// Utils ////

    internal fun resolveWidth(contentWidth: Int, availableWidth: Int): Int {
        val currentAvailable = UiRuntime.currentRuntime?.currentAvailableWidth

        return width.resolve(this.minSize.width, padding.horizontal, currentAvailable, contentWidth, availableWidth)
    }

    internal fun resolveHeight(contentHeight: Int, availableHeight: Int): Int {
        val currentAvailable = UiRuntime.currentRuntime?.currentAvailableHeight

        return height.resolve(this.minSize.height, padding.vertical, currentAvailable, contentHeight, availableHeight)
    }
}