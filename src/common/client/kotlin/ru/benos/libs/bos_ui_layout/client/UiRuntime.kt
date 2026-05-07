package ru.benos.libs.bos_ui_layout.client

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import org.joml.Vector2i

class UiRuntime(
    var guiGraphics: GuiGraphics,
    var font       : Font,
    var mouse      : Vector2i
) {
    constructor(guiGraphics: GuiGraphics, font: Font, mouseX: Int, mouseY: Int):
            this(guiGraphics, font, Vector2i(mouseX, mouseY))

    companion object {
        var currentRuntime: UiRuntime? = null
    }

    var deltaTime: Float = 0.0f
    var totalTime: Float = 0.0f
    var lastFrameTimeNanos: Long? = null

    fun newFrame(guiGraphics: GuiGraphics, font: Font, mouseX: Int, mouseY: Int) {
        // Cleanup //

        // Update datas //
        this.guiGraphics = guiGraphics
        this.font = font
        this.mouse = Vector2i(mouseX, mouseY)

        // Apply //
        currentRuntime = this
    }
}