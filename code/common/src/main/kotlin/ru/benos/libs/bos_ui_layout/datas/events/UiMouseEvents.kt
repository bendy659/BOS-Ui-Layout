package ru.benos.libs.bos_ui_layout.datas.events

data class UiMouseEvents(
    val onEntered: (() -> Unit)?,
    val onHovered: ((Int, Int) -> Unit)?,
    val onExisted: (() -> Unit)?,

    val onClicked: ((Int, Int, Int) -> Boolean)?,
    val onReleased: ((Int, Int, Int) -> Boolean)?,

    val onDragged: ((Int, Double, Double) -> Boolean)?,
    val onScrolled: ((Boolean, Double, Double) -> Boolean)?,
) {
    class Builder {
        var onEntered: (() -> Unit)? = null
        var onHovered: ((Int, Int) -> Unit)? = null
        var onExisted: (() -> Unit)? = null

        var onClicked: ((Int, Int, Int) -> Boolean)? = null
        var onReleased: ((Int, Int, Int) -> Boolean)? = null

        var onDragged: ((Int, Double, Double) -> Boolean)? = null
        var onScrolled: ((Boolean, Double, Double) -> Boolean)? = null

        fun build(): UiMouseEvents =
            UiMouseEvents(onEntered, onHovered, onExisted, onClicked, onReleased, onDragged, onScrolled)

        fun onEntered(block: () -> Unit) {
            this.onEntered = block
        }

        fun onHovered(block: (Int, Int) -> Unit) {
            this.onHovered = block
        }

        fun onExisted(block: () -> Unit) {
            this.onExisted = block
        }

        fun onClicked(block: (Int, Int, Int) -> Boolean) {
            this.onClicked = block
        }

        fun onReleased(block: (Int, Int, Int) -> Boolean) {
            this.onReleased = block
        }

        fun onDragged(block: (Int, Double, Double) -> Boolean) {
            this.onDragged = block
        }

        fun onScrolled(block: (Boolean, Double, Double) -> Boolean) {
            this.onScrolled = block
        }
    }

    companion object {
        val EMPTY: UiMouseEvents
            get() = UiMouseEvents(
                { /* Nothing */ }, { _, _ -> /* Nothing */ }, { /* Nothing */ },
                { _, _, _ -> false }, { _, _, _ -> false },
                { _, _, _ -> false }, { _, _, _ -> false }
            )
    }
}