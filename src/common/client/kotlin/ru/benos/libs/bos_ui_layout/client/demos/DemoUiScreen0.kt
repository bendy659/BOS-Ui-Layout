package ru.benos.libs.bos_ui_layout.client.demos

import ru.benos.libs.bos_ui_layout.client.AbstractBosUiLayout
import ru.benos.libs.bos_ui_layout.client.api.UiStretch
import ru.benos.libs.bos_ui_layout.client.builders.UiBuilder
import ru.benos.libs.bos_ui_layout.client.datas.UiBoxTheme
import ru.benos.libs.bos_ui_layout.client.datas.UiModifier

class DemoUiScreen0: AbstractBosUiLayout() {
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