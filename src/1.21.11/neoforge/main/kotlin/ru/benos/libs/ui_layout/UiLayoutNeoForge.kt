package ru.benos.libs.ui_layout

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent

@Mod(UiLayout.MOD_ID)
@EventBusSubscriber(modid = UiLayout.MOD_ID)
class UiLayoutNeoForge {
    val LOGGER = UiLayout.LOGGER

    companion object {
        @JvmStatic
        @SubscribeEvent
        fun onCommonSetup(event: FMLCommonSetupEvent) {
            UiLayout.launch("NeoForge")

            // Other for NeoForge //
        }

        @JvmStatic
        @SubscribeEvent
        fun onClientSetup(event: FMLClientSetupEvent) {
            UiLayout.launchClient("NeoForge")

            // Other for NeoForge client //
        }
    }
}