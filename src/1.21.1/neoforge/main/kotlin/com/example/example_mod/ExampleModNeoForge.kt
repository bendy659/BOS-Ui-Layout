package com.example.example_mod

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent

@Mod(ExampleMod.MOD_ID)
@EventBusSubscriber(modid = ExampleMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
class ExampleModNeoForge {
    val LOGGER = ExampleMod.LOGGER

    companion object {
        @JvmStatic
        @SubscribeEvent
        fun onCommonSetup(event: FMLCommonSetupEvent) {
            ExampleMod.launch("NeoForge")

            // Other for NeoForge //
        }

        @JvmStatic
        @SubscribeEvent
        fun onClientSetup(event: FMLClientSetupEvent) {
            ExampleMod.launchClient("NeoForge")

            // Other for NeoForge client //
        }
    }
}