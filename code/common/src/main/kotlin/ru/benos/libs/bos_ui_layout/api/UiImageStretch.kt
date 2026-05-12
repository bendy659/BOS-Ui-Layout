package ru.benos.libs.bos_ui_layout.api

import ru.benos.libs.bos_ui_layout.datas.IUiImageStretch
import ru.benos.libs.bos_ui_layout.enum.UIImageStretchMode

object UiImageStretch {
    fun stretch(): IUiImageStretch =
        IUiImageStretch.Stretch

    fun expand(mode: UIImageStretchMode = UIImageStretchMode.AVAILABLE): IUiImageStretch =
        when (mode) {
            UIImageStretchMode.AVAILABLE -> IUiImageStretch.Expand
            UIImageStretchMode.WIDTH     -> IUiImageStretch.ExpandWidth
            UIImageStretchMode.HEIGHT    -> IUiImageStretch.ExpandHeight
        }

    fun fit(): IUiImageStretch =
        IUiImageStretch.Fit
}