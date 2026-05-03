package ru.benos.libs.ui_layout

import ru.benos.libs.ui_layout.builder.UiBuilder

expect abstract class AbstractUiLayout() {
    abstract fun UiBuilder.ui()
}