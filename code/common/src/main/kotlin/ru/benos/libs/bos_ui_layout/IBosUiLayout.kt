package ru.benos.libs.bos_ui_layout

import ru.benos.libs.bos_ui_layout.builders.UiBuilder
import ru.benos.libs.bos_ui_layout.datas.base.UiRect

interface IBosUiLayout {
    var runtime: UiRuntime?
    val contentBounds: UiRect

    fun UiBuilder.ui()
}