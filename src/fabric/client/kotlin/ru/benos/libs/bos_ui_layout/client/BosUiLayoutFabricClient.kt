package ru.benos.libs.bos_ui_layout.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import org.lwjgl.glfw.GLFW
import ru.benos.libs.bos_ui_layout.BosUiLayout
import ru.benos.libs.bos_ui_layout.client.demos.DemoUiScreen0

object BosUiLayoutFabricClient: ClientModInitializer {
    val KEY_X: KeyMapping = KeyMapping(
        "key.bos_ui_layout.open_demo",
        GLFW.GLFW_KEY_X,
        "key.categories.bos_ui_layout"
    )

    override fun onInitializeClient() {
        BosUiLayout.launchClient("Fabric")

        // Other for Fabric client //
        KeyBindingHelper.registerKeyBinding(KEY_X)

        ClientTickEvents.END_CLIENT_TICK.register { _ ->
            if (KEY_X.consumeClick())
                Minecraft.getInstance().setScreen(DemoUiScreen0())
        }
    }
}