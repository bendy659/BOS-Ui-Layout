package ru.benos.libs.bos_ui_layout.datas.ramp

import kotlin.math.pow

abstract class AbstractUiRamp<T>(val keys: List<UiRampKey<T>>) {
    class UiRampBuilder<T> {
        private val keys: MutableList<UiRampKey<T>> = mutableListOf()

        fun key(time: Float, value: T, easing: Float = 1f) =
            keys.add(UiRampKey(time, value, easing))

        fun buildKeys(): List<UiRampKey<T>> = keys.sortedBy { it.time }
    }

    abstract fun lerp(t: Float, a: T, b: T): T

    fun get(t: Float): T {
        if (keys.isEmpty()) throw IllegalStateException("Ramp has no keys")
        if (keys.size == 1) return keys.first().value

        val clamped = t.coerceIn(keys.first().time, keys.last().time)

        val fromIndex = keys.indexOfLast { it.time <= clamped }
        val toIndex = (fromIndex + 1).coerceAtMost(keys.lastIndex)

        if (fromIndex == toIndex) return keys[fromIndex].value

        val from = keys[fromIndex]
        val to = keys[toIndex]

        val localT = (clamped - from.time) / (to.time - from.time)
        val easedT = applyEasing(localT, from.easing)

        return lerp(easedT, from.value, to.value)
    }

    protected fun applyEasing(t: Float, easing: Float): Float =
        when {
            easing == 1f -> t
            easing >= 0f -> t.pow(easing)
            else -> {
                if (t < 0.5f)
                    0.5f * (2f * t).pow(-easing)
                else
                    1f - 0.5f * (2f * (1f - t)).pow(-easing)
            }
        }
}