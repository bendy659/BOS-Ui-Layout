package com.example.example_mod.client

import com.example.example_mod.ExampleMod
import net.fabricmc.api.ClientModInitializer

object ExampleModFabricClient: ClientModInitializer {
    override fun onInitializeClient() {
        ExampleMod.launchClient("Fabric")

        // Other for Fabric client //
    }
}