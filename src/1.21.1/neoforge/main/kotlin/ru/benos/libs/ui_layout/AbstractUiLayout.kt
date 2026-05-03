package ru.benos.libs.ui_layout

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import ru.benos.libs.ui_layout.builder.UiBuilder
import ru.benos.libs.ui_layout.data.UiBoxTheme
import ru.benos.libs.ui_layout.data.UiModifier
import ru.benos.libs.ui_layout.data.UiRect
import ru.benos.libs.ui_layout.nodes.IUiNode
import ru.benos.libs.ui_layout.nodes.UiBoxNode

actual abstract class AbstractUiLayout: Screen(Component.empty()), IUiLayout {
    override var runtime: UiRuntime? = null

    override val contentBounds: UiRect
        get() = UiRect(0, 0, width, height)

    override fun buildUi(children: UiBuilder.() -> Unit): IUiNode =
        UiBoxNode(
            boxTheme = UiBoxTheme.TRANSPARENT,
            children = UiBuilder().apply { children() }.build(),
            enableScissor = false,
            modifier = UiModifier
        )

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) =
        UiLayout.renderUi(guiGraphics, mouseX, mouseY, font, ::runtime) { buildUi { ui() }.render(it, contentBounds) }

    override fun mouseClicked(mouseX: Double, mouseY: Double, key: Int): Boolean {
        val click = runtime?.clicked(key, mouseX, mouseY)
        if (click == true)
            return true

        return super.mouseClicked(mouseX, mouseY, key)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, key: Int): Boolean {
        val click = runtime?.clicked(key, mouseX, mouseY)
        if (click == true)
            return true

        return super.mouseReleased(mouseX, mouseY, key)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, key: Int, deltaX: Double, deltaY: Double): Boolean =
        super.mouseDragged(mouseX, mouseY, key, deltaX, deltaY)

    override fun mouseScrolled(mouseX: Double, mouseY: Double, deltaX: Double, deltaY: Double): Boolean =
        super.mouseScrolled(mouseX, mouseY, deltaX, deltaY)

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean =
        super.keyPressed(keyCode, scanCode, modifiers)
}