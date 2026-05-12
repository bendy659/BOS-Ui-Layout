package ru.benos.libs.bos_ui_layout.datas

import kotlin.math.max

sealed interface IUiStretch {
    sealed interface Weighted : IUiStretch {
        val weight: Float
    }

    data class Fixed(val value: Int) : IUiStretch
    data class Fill(override val weight: Float = 1f) : Weighted
    data class Available(override val weight: Float = 1f) : Weighted
    data class Expand(override val weight: Float = 1f) : Weighted

    data object Fit : IUiStretch

    fun resolve(min: Int, padding: Int, currentAvailable: Int?, content: Int, available: Int): Int {
        val b =
            when (this) {
                is Fixed -> this.value
                is Fill -> (available * weight).toInt()
                is Available -> ((currentAvailable ?: available) * weight).toInt()
                is Expand -> {
                    val c = content + padding
                    val d = currentAvailable ?: available

                    max(c, d)
                }

                Fit -> content + padding
            }

        return max(min, b)
    }

    fun calcLength(inner: Int, current: Int?, measure: Int) =
        when (this) {
            is Fill -> (inner * weight).toInt()
            is Available -> ((current ?: inner) * weight).toInt()
            is Expand -> (measure * weight).toInt()
            else -> measure.coerceAtMost(inner)
        }
}
