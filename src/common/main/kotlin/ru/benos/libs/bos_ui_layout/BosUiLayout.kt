package ru.benos.libs.bos_ui_layout

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object BosUiLayout {
    const val MOD_ID: String = "bos_ui_layout"
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

    fun launch(loader: String) {
        LOGGER.info("Now running in \"$loader\"...")
    }
    fun launchClient(loader: String) {
        LOGGER.info("Now running in \"$loader\" Client...")
    }
}