package ru.benos.libs.bos_ui_layout.demos

import net.minecraft.network.chat.Component
import org.joml.Vector2i
import ru.benos.libs.bos_ui_layout.api.AbstractBosUiLayoutScreen
import ru.benos.libs.bos_ui_layout.api.UiStretch
import ru.benos.libs.bos_ui_layout.builders.UiBuilder
import ru.benos.libs.bos_ui_layout.datas.UiBoxTheme
import ru.benos.libs.bos_ui_layout.datas.UiModifier
import ru.benos.libs.bos_ui_layout.enum.UiAlign
import ru.benos.libs.bos_ui_layout.enum.UiTextAlign
import kotlin.math.roundToInt

class DemoUiScreen0 : AbstractBosUiLayoutScreen() {
    val boxPos: Vector2i = Vector2i(16, 16)

    override fun UiBuilder.ui() {
        val t = (runtime?.totalTime ?: 0.0f) * 0.5f

        box(
            boxTheme = UiBoxTheme.RED,
            modifier = UiModifier
                .align(UiAlign.Center, UiAlign.Center)
                .padding(1)
                .transform {
                    origin(0.5f, 0.5f, 0.0f)
                    rotation(t, t, 0.0f)
                    scale(4.0f, 4.0f)
                }
        ) {
            label(
                component = Component.literal("t: ${"%.3f".format(t)}"),
                textAlign = UiTextAlign.Center,
                modifier = UiModifier
                    .align(UiAlign.Center, UiAlign.Center)
                    .transform { }
            )
        }

        box(
            boxTheme = UiBoxTheme.BLACK,
            modifier = UiModifier
                .overridePosition(boxPos.x, boxPos.y)
                .width(UiStretch.fixed(64))
                .height(UiStretch.fixed(64))
                .mouseEvents {
                    onDragged { _, deltaX, deltaY ->
                        boxPos.x += deltaX.roundToInt()
                        boxPos.y += deltaY.roundToInt()

                        true
                    }
                }
        )
    }
}
