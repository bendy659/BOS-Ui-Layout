package ru.benos.libs.bos_ui_layout.datas

import ru.benos.libs.bos_ui_layout.datas.base.UiRect
import ru.benos.libs.bos_ui_layout.datas.base.UiSize

sealed interface IUiImageStretch {
    data object Stretch     : IUiImageStretch
    data object Expand      : IUiImageStretch
    data object ExpandWidth : IUiImageStretch
    data object ExpandHeight: IUiImageStretch
    data object Fit         : IUiImageStretch

    fun calcRect(image: UiSize, available: UiRect): UiRect {
        if (image.width <= 0 || image.height <= 0 || available.width <= 0 || available.height <= 0) {
            return UiRect(available.x, available.y, 0, 0)
        }

        val size = when (this) {
            Stretch -> available.size

            Fit -> {
                val scale = minOf(
                    available.width.toDouble() / image.width.toDouble(),
                    available.height.toDouble() / image.height.toDouble()
                )

                UiSize(
                    width = (image.width * scale).toInt(),
                    height = (image.height * scale).toInt()
                )
            }

            Expand -> {
                val scale = maxOf(
                    available.width.toDouble() / image.width.toDouble(),
                    available.height.toDouble() / image.height.toDouble()
                )

                UiSize(
                    width = (image.width * scale).toInt(),
                    height = (image.height * scale).toInt()
                )
            }

            ExpandWidth -> {
                val scale = available.width.toDouble() / image.width.toDouble()

                UiSize(
                    width = available.width,
                    height = (image.height * scale).toInt()
                )
            }

            ExpandHeight -> {
                val scale = available.height.toDouble() / image.height.toDouble()

                UiSize(
                    width = (image.width * scale).toInt(),
                    height = available.height
                )
            }
        }

        val x = available.x + (available.width - size.width) / 2
        val y = available.y + (available.height - size.height) / 2

        return UiRect(x, y, size)
    }
}