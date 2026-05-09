package ru.benos.libs.bos_ui_layout.datas

import ru.benos.libs.bos_ui_layout.datas.base.UiSize

data class UIScrollState(
    var scrollX: Int = 0,
    var scrollY: Int = 0,
    var contentSize: UiSize = UiSize.ZERO
)
