package ru.benos.libs.bos_ui_layout.demos

import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import ru.benos.libs.bos_ui_layout.api.AbstractBosUiHudLayout
import ru.benos.libs.bos_ui_layout.api.UiCanvas
import ru.benos.libs.bos_ui_layout.api.UiRamp
import ru.benos.libs.bos_ui_layout.api.UiStretch
import ru.benos.libs.bos_ui_layout.builders.UiBuilder
import ru.benos.libs.bos_ui_layout.datas.UiBoxTheme
import ru.benos.libs.bos_ui_layout.datas.UiModifier
import ru.benos.libs.bos_ui_layout.datas.base.UiColor
import ru.benos.libs.bos_ui_layout.datas.ramp.UiRampColor
import ru.benos.libs.bos_ui_layout.enum.UiAlign
import ru.benos.libs.bos_ui_layout.enum.UiTextAlign

object DemoUiHud0: AbstractBosUiHudLayout() {
    val healthColorRamp: UiRampColor = UiRamp.color {
        key(0.0f, UiColor.RED)
        key(0.5f, UiColor.YELLOW)
        key(1.0f, UiColor.GREEN)
    }

    val exampleShader = UiBoxTheme(
        UiCanvas.shader("bos_ui_layout:hologram") { nodeCtx ->
            getUniform("Time").value(nodeCtx.runtime.totalTime)
            getUniform("Size").vec2(nodeCtx.bounds.width.toFloat(), nodeCtx.bounds.height.toFloat())
            getUniform("Offset").vec2(nodeCtx.bounds.x.toFloat(), nodeCtx.bounds.y.toFloat())
        }
    )

    override fun UiBuilder.ui() {
        val player: Player = Minecraft.getInstance().player ?: return
        val percent = player.health / player.maxHealth
        val percentStr = "%.0f".format(percent * 100)

        box(
            boxTheme = UiBoxTheme.BLACK,
            modifier = UiModifier
                .align(UiAlign.Start, UiAlign.End)
                .padding(1)
                .width(UiStretch.expand(0.25f))
                .transform {
                    origin(0.0f, 1.0f, 0.1f)
                    offset(16.0f, -32.0f, 0.0f)
                    rotationDegrees(-25.0f, 25.0f, -15.0f)
                    scale(1.5f, 1.5f)
                }
        ) {
            box(
                boxTheme = exampleShader,
                modifier = UiModifier
                    .align(UiAlign.Start, UiAlign.Center)
                    .width(UiStretch.expand(percent))
                    .height(UiStretch.expand())
                    .transform {
                        offset(4.0f, 4.0f, 0.1f)
                    }
            )

            label(
                component = Component.literal("Health: $percentStr%)"),
                textAlign = UiTextAlign.Center,
                modifier = UiModifier
                    .align(UiAlign.Start, UiAlign.Center)
                    .transform {
                        offset(0.0f, 0.0f, 1.0f)
                    }
            )
        }
    }
}