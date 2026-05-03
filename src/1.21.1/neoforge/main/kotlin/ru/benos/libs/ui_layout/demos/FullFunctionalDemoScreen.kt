package ru.benos.libs.ui_layout.demos

import ru.benos.libs.ui_layout.AbstractUiLayout
import ru.benos.libs.ui_layout.builder.UiBuilder

class FullFunctionalDemoScreen: AbstractUiLayout() {
    override fun UiBuilder.ui() =
        fullFunctionalDemoUiScreenCommon()
}