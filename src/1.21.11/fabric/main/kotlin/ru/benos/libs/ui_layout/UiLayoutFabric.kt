package ru.benos.libs.ui_layout

import net.fabricmc.api.ModInitializer

object UiLayoutFabric: ModInitializer {
    override fun onInitialize() {
        UiLayout.launch("Fabric")

        // Other for Fabric //
    }
}