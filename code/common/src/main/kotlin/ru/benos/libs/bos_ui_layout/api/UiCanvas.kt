package ru.benos.libs.bos_ui_layout.api

import net.minecraft.client.renderer.ShaderInstance
import ru.benos.libs.bos_ui_layout.UiShaderCache
import ru.benos.libs.bos_ui_layout.datas.IUiCanvas
import ru.benos.libs.bos_ui_layout.datas.base.UiColor
import ru.benos.libs.bos_ui_layout.datas.base.UiNodeContext
import ru.benos.libs.bos_ui_layout.datas.base.UiRect

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

    fun shader(resource: String, block: UiShaderCache.Builder.(UiNodeContext) -> Unit): IUiCanvas.Shader =
        IUiCanvas.Shader(UiColor.WHITE, resource, block)
}