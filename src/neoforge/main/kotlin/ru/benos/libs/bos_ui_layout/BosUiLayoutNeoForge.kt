package ru.benos.libs.bos_ui_layout

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent

@Mod(BosUiLayout.MOD_ID)
@EventBusSubscriber(modid = BosUiLayout.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
class BosUiLayoutNeoForge {
    val LOGGER = BosUiLayout.LOGGER

    companion object {
        @JvmStatic
        @SubscribeEvent
        fun onCommonSetup(event: FMLCommonSetupEvent) {
            BosUiLayout.launch("NeoForge")

            // Other for NeoForge //
        }

        @JvmStatic
        @SubscribeEvent
        fun onClientSetup(event: FMLClientSetupEvent) {
            BosUiLayout.launchClient("NeoForge")

            // Other for NeoForge client //
        }
    }
}