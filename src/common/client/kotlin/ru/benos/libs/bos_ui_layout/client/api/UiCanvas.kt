package ru.benos.libs.bos_ui_layout.client.api

import ru.benos.libs.bos_ui_layout.client.datas.IUiCanvas
import ru.benos.libs.bos_ui_layout.client.datas.UiColor
import ru.benos.libs.bos_ui_layout.client.datas.UiRect

object UiCanvas {
    fun fill(color: UiColor): IUiCanvas =
        IUiCanvas.Fill(color)
    fun fill(r: Int, g: Int, b: Int, a: Int = 255): IUiCanvas =
        fill(UiColor(r, g, b, a))

    fun gradient(colorStart: UiColor, colorEnd: UiColor): IUiCanvas =
        IUiCanvas.Gradient(colorStart, colorEnd)

    fun outline(color: UiColor, width: Int = 1): IUiCanvas.Outline =
        IUiCanvas.Outline(color, width)
    fun outline(r: Int, g: Int, b: Int, a: Int = 255, width: Int = 1): IUiCanvas.Outline =
        outline(UiColor(r, g, b, a), width)

    fun texture(color: UiColor, id: String, uv: UiRect = UiRect.X16): IUiCanvas.Texture =
        IUiCanvas.Texture(color, id, uv)

    fun nineSliceTexture(color: UiColor, id: String): IUiCanvas.NineSliceTexture =
        IUiCanvas.NineSliceTexture(color, id)

    fun shader(resource: String, args: Map<String, Any> = emptyMap()): IUiCanvas.Shader =
        IUiCanvas.Shader(UiColor.WHITE, resource)
}