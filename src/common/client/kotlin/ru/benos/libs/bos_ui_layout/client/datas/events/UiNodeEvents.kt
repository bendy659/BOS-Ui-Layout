package ru.benos.libs.bos_ui_layout.client.datas.events

data class UiNodeEvents(
    val onFocused: (() -> Unit)?
) {
    class Builder {
        var onFocused: (() -> Unit)? = null

        fun build(): UiNodeEvents =
            UiNodeEvents(onFocused)

        fun onFocused(block: () -> Unit) {
            this.onFocused = block
        }
    }

    companion object {
        val EMPTY: UiNodeEvents
            get() = UiNodeEvents(null)
    }
}