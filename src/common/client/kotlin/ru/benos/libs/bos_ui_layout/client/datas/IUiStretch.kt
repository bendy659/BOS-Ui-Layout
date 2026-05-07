package ru.benos.libs.bos_ui_layout.client.datas

sealed interface IUiStretch {
    sealed interface Weighted: IUiStretch { val weight: Float }

    data class Fixed    (val value: Int) : IUiStretch
    data class Fill     (override val weight: Float = 1f): Weighted
    data class Available(override val weight: Float = 1f): Weighted
    data class Expand   (override val weight: Float = 1f): Weighted

    data object Wrap: IUiStretch

    fun resolve(min: Int, padding: Int, currentAvailable: Int?, content: Int, available: Int): Int {
        val b =
            when (this) {
                is Fixed     -> this.value
                is Fill      -> available
                is Available -> currentAvailable ?: available
                is Expand    -> {
                    val c = content + padding
                    val d = currentAvailable ?: available

                    kotlin.math.max(c, d)
                }
                Wrap -> content + padding
            }

        return kotlin.math.max(min, b)
    }

    fun calcLength(inner: Int, current: Int?, measure: Int) =
        when (this) {
            is Fill      -> inner
            is Available -> current ?: inner
            is Expand    -> measure
            else -> measure.coerceAtMost(inner)
        }
}
