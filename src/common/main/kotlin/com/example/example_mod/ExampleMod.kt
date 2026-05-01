package com.example.example_mod

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ExampleMod {
    const val MOD_ID = "example_mod"

    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

    fun launch(loader: String) {
        LOGGER.info("Now launch in \"$loader\"...")

        LOGGER.info(test())
    }

    fun launchClient(loader: String) {
        LOGGER.info("Now launch client in \"$loader\"...")
    }
}