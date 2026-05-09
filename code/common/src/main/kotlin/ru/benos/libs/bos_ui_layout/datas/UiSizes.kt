package ru.benos.libs.bos_ui_layout.datas

import ru.benos.libs.bos_ui_layout.BosUiUtils.uiPx

data class UiSizes(
    val width: IUiSizes,
    val height: IUiSizes
) {
    companion object {
        val ZERO: UiSizes
            get() = UiSizes(0.uiPx, 0.uiPx)
    }
}