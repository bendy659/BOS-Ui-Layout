package ru.benos.libs.ui_layout.data

sealed interface IUiLength {
    sealed interface IUiWeighted: IUiLength { val weight: Float }

    data object Wrap                : IUiLength
    data class Fixed(val value: Int): IUiLength

    data class Fill     (override val weight: Float = 1.0f): IUiWeighted
    data class Available(override val weight: Float = 1.0f): IUiWeighted
    data class Expand   (override val weight: Float = 1.0f): IUiWeighted

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
}