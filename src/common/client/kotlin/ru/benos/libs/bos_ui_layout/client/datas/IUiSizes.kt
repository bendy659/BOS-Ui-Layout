package ru.benos.libs.bos_ui_layout.client.datas

sealed interface IUiSizes {
    val result: Int

    data class Const(val value: Int) : IUiSizes {
        override val result: Int
            get() = value
    }

    data class Percent(val percent: Float) : IUiSizes {
        override val result: Int
            get() = TODO("Not yet implemented")
    }
}