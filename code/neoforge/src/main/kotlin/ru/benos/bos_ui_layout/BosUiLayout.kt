package ru.benos.bos_ui_layout

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent

@Mod(BosUiLayout.MOD_ID)
@EventBusSubscriber(modid = BosUiLayout.MOD_ID)
class BosUiLayout {
    companion object {
        const val MOD_ID: String = "bos_ui_layout"

        @JvmStatic
        @SubscribeEvent
        fun onClientSetup(event: FMLClientSetupEvent) { }
    }
}