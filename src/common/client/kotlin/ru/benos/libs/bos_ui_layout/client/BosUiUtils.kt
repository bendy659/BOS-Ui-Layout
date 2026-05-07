package ru.benos.libs.bos_ui_layout.client

import ru.benos.libs.bos_ui_layout.client.datas.IUiSizes

object BosUiUtils {
    val Int.uiPx: IUiSizes
        get() = IUiSizes.Const(this@uiPx)

    val Float.uiPct: IUiSizes
        get() = IUiSizes.Percent(this@uiPct)
}