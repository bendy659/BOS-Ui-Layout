package ru.benos.libs.bos_ui_layout.client.datas.events

data class UiKeyEvents(
    val onPressed : ((key: Int) -> Boolean)?,
    val onReleased: ((key: Int) -> Boolean)?,

    val onCharTyped: ((char: Char) -> Boolean)?
) {
    class Builder {
        var onPressed : ((key: Int) -> Boolean)? = null
        var onReleased: ((key: Int) -> Boolean)? = null
        var onCharTyped: ((char: Char) -> Boolean)? = null

        fun build(): UiKeyEvents =
            UiKeyEvents(onPressed, onReleased, onCharTyped)

        fun onPressed(block: (key: Int) -> Boolean) {
            this.onPressed = block
        }
        fun onReleased(block: (key: Int) -> Boolean) {
            this.onReleased = block
        }
        fun onCharTyped(block: (char: Char) -> Boolean) {
            this.onCharTyped = block
        }
    }

    companion object {
        val EMPTY: UiKeyEvents
            get() = UiKeyEvents(
                null, null,
                null
            )
    }
}