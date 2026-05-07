package ru.benos.libs.bos_ui_layout.client

import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.KeyMapping
import ru.benos.libs.bos_ui_layout.BosUiLayout

object BosUiLayoutFabricClient: ClientModInitializer {
    lateinit var KEY_X: KeyMapping

    override fun onInitializeClient() {
        BosUiLayout.launchClient("Fabric")

        // Other for Fabric client //
    }
}