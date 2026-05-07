package ru.benos.libs.bos_ui_layout

import net.fabricmc.api.ModInitializer

object BosUiLayoutFabric: ModInitializer {
    override fun onInitialize() {
        BosUiLayout.launch("Fabric")

        // Other for Fabric //
    }
}