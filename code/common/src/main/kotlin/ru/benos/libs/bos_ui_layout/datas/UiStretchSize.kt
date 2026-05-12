package ru.benos.libs.bos_ui_layout.datas

data class UiStretchSize(
    val width: IUiStretch,
    val height: IUiStretch
) {
    constructor(stretch: IUiStretch) :
            this(stretch, stretch)

    companion object {
        val WRAP: UiStretchSize
            get() = UiStretchSize(IUiStretch.Fit)
    }
}
