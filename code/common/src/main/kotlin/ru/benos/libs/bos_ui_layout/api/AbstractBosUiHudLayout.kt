package ru.benos.libs.bos_ui_layout.api

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import ru.benos.libs.bos_ui_layout.BosUiLayoutCore
import ru.benos.libs.bos_ui_layout.IBosUiLayout
import ru.benos.libs.bos_ui_layout.UiRuntime
import ru.benos.libs.bos_ui_layout.datas.base.UiRect

abstract class AbstractBosUiHudLayout: IBosUiLayout {
    val size: UiRect
        get() = UiRect.stretch(
            Minecraft.getInstance().window.guiScaledWidth,
            Minecraft.getInstance().window.guiScaledHeight
        )

    protected val core: BosUiLayoutCore = BosUiLayoutCore(::size)

    override var runtime: UiRuntime?
        get() = core.runtime
        set(value) { core.runtime = value }

    override val contentBounds: UiRect = UiRect.ZERO

    fun render(guiGraphics: GuiGraphics) =
        core.render(guiGraphics, Minecraft.getInstance().font, 0, 0) { this.ui() }
}