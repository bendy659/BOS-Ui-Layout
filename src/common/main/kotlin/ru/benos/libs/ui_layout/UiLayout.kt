package ru.benos.libs.ui_layout

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.benos.libs.ui_layout.builder.UiBuilder
import kotlin.reflect.KMutableProperty0

object UiLayout {
    const val MOD_ID = "example_mod"

    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

    fun launch(loader: String) {
        LOGGER.info("Now launch in \"$loader\"...")
    }

    fun launchClient(loader: String) {
        LOGGER.info("Now launch client in \"$loader\"...")
    }

    // Utils //

    fun renderUi(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, font: Font, runtime: KMutableProperty0<UiRuntime?>, block: (frameRuntime: UiRuntime) -> Unit) {
        // Update frame data //
        val frameRuntime = runtime.get() ?: UiRuntime(guiGraphics, font, mouseX, mouseY)
        runtime.set(frameRuntime)

        // Ticking delta time //
        val now = System.nanoTime()
        val lastFrameTime = frameRuntime.lastFrameTimeNanos

        frameRuntime.deltaTime =
            if (lastFrameTime == null) 0.0f
            else ((now - lastFrameTime) / 10_000_000_000).toFloat()
        frameRuntime.totalTime += frameRuntime.deltaTime
        frameRuntime.lastFrameTimeNanos = now
        frameRuntime.newFrame(guiGraphics, font, mouseX, mouseY)

        block(frameRuntime)
    }
}