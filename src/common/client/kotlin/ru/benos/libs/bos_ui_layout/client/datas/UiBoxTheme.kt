package ru.benos.libs.bos_ui_layout.client.datas

import ru.benos.libs.bos_ui_layout.client.api.UiCanvas

data class UiBoxTheme(
    var layersNormal  : Set<IUiCanvas>,
    var layersClicked : Set<IUiCanvas> = layersNormal,
    var layersHovered : Set<IUiCanvas> = layersNormal,
    var layersReleased: Set<IUiCanvas> = layersNormal,
    var layersFocused : Set<IUiCanvas> = layersNormal
) {
    constructor(fill: IUiCanvas):
            this(setOf(fill))

    companion object {
        val TRANSPARENT: UiBoxTheme
            get() = UiBoxTheme(UiCanvas.fill(0, 0, 0, 0))

        val BLACK: UiBoxTheme
            get() = UiBoxTheme(
                layersNormal = setOf(
                    UiCanvas.fill(16, 16, 16),
                    UiCanvas.outline(255, 255, 255, 16, 2)
                )
            )

        val RED: UiBoxTheme
            get() = UiBoxTheme(
                layersNormal = setOf(
                    UiCanvas.fill(255, 0, 0),
                    UiCanvas.outline(255, 64, 64, width = 2)
                )
            )
    }

    fun normal(vararg layer: IUiCanvas): UiBoxTheme {
        this.layersNormal = layer.toSet()
        return this
    }

    fun clicked(vararg layer: IUiCanvas): UiBoxTheme {
        this.layersClicked = layer.toSet()
        return this
    }

    fun hovered(vararg layer: IUiCanvas): UiBoxTheme {
        this.layersHovered = layer.toSet()
        return this
    }

    fun released(vararg layer: IUiCanvas): UiBoxTheme {
        this.layersReleased = layer.toSet()
        return this
    }

    fun focused(vararg layer: IUiCanvas): UiBoxTheme {
        this.layersFocused = layer.toSet()
        return this
    }
}