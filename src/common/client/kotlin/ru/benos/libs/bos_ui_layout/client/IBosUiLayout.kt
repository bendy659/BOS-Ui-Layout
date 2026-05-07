package ru.benos.libs.bos_ui_layout.client

import ru.benos.libs.bos_ui_layout.client.builders.UiBuilder
import ru.benos.libs.bos_ui_layout.client.datas.UiRect

interface IBosUiLayout {
    var runtime: UiRuntime?
    val contentBounds: UiRect

    fun UiBuilder.ui()
}