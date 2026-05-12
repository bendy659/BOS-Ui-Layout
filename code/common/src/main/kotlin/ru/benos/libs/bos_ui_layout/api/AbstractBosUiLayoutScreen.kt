package ru.benos.libs.bos_ui_layout.api

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import ru.benos.libs.bos_ui_layout.BosUiLayoutCore
import ru.benos.libs.bos_ui_layout.IBosUiLayout
import ru.benos.libs.bos_ui_layout.UiRuntime
import ru.benos.libs.bos_ui_layout.datas.base.UiRect

abstract class AbstractBosUiLayoutScreen : Screen(Component.empty()), IBosUiLayout {
    private val size: UiRect
        get() = UiRect.stretch(width, height)

    private val core: BosUiLayoutCore = BosUiLayoutCore(::size)

    override var runtime: UiRuntime?
        get() = core.runtime
        set(value) { core.runtime = value }

    override val contentBounds: UiRect
        get() = UiRect.ZERO

    // Overrides //

    override fun render(p0: GuiGraphics, p1: Int, p2: Int, p3: Float) =
        core.render(p0, font, p1, p2) { this.ui() }

    override fun mouseClicked(p0: Double, p1: Double, p2: Int): Boolean {
        val mouseClicked = runtime?.mouseClicked(p2, p0, p1)
        if (mouseClicked == true)
            return true

        return super.mouseClicked(p0, p1, p2)
    }

    override fun mouseReleased(p0: Double, p1: Double, p2: Int): Boolean {
        val mouseReleased = runtime?.mouseReleased(p2, p0, p1)
        if (mouseReleased == true)
            return true

        return super.mouseReleased(p0, p1, p2)
    }

    override fun mouseDragged(p0: Double, p1: Double, p2: Int, p3: Double, p4: Double): Boolean {
        val mouseDragged = runtime?.mouseDragged(p2, p0, p1, p3, p4)
        if (mouseDragged == true)
            return true

        return super.mouseDragged(p0, p1, p2, p3, p4)
    }

    override fun mouseScrolled(p0: Double, p1: Double, p2: Double, p3: Double): Boolean {
        val mouseScrolled = runtime?.mouseScrolled(p0, p1, p2, p3)
        if (mouseScrolled == true)
            return true

        return super.mouseScrolled(p0, p1, p2, p3)
    }

    override fun keyPressed(p0: Int, p1: Int, p2: Int): Boolean {
        return super.keyPressed(p0, p1, p2)
    }

    override fun keyReleased(p0: Int, p1: Int, p2: Int): Boolean {
        return super.keyReleased(p0, p1, p2)
    }
}
