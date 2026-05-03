package ru.benos.libs.ui_layout.client

import ru.benos.libs.ui_layout.UiLayout
import net.fabricmc.api.ClientModInitializer

object UiLayoutFabricClient: ClientModInitializer {
    override fun onInitializeClient() {
        UiLayout.launchClient("Fabric")

        // Other for Fabric client //
    }
}