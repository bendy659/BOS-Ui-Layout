package ru.benos.libs.bos_ui_layout.demos

import ru.benos.libs.bos_ui_layout.AbstractBosUiLayout
import ru.benos.libs.bos_ui_layout.api.UiStretch
import ru.benos.libs.bos_ui_layout.builders.UiBuilder
import ru.benos.libs.bos_ui_layout.datas.UiBoxTheme
import ru.benos.libs.bos_ui_layout.datas.UiModifier

class DemoUiScreen0 : AbstractBosUiLayout() {
    override fun UiBuilder.ui() {
        box(
            boxTheme = UiBoxTheme.RED,
            modifier = UiModifier
                .width(UiStretch.fixed(256))
                .height(UiStretch.fixed(256))
                .transform {
                    rotation(25.0f, 25.0f, 25.0f)
                }
        ) {

        }
    }
}