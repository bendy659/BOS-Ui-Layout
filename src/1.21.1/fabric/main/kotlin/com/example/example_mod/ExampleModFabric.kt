package com.example.example_mod

import net.fabricmc.api.ModInitializer

object ExampleModFabric: ModInitializer {
    override fun onInitialize() {
        ExampleMod.launch("Fabric")

        // Other for Fabric //
    }
}