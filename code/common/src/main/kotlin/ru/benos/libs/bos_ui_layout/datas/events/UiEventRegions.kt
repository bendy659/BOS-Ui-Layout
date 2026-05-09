package ru.benos.libs.bos_ui_layout.datas.events

import ru.benos.libs.bos_ui_layout.datas.base.UiRect
import ru.benos.libs.bos_ui_layout.datas.base.UiTransform

data class UiEventRegion0<R>(
    val rect: UiRect,
    val transform: UiTransform,
    val event: () -> R
)

data class UiEventRegion1<A, R>(
    val rect: UiRect,
    val transform: UiTransform,
    val event: (A) -> R
)

data class UiEventRegions2<A, B, R>(
    val rect: UiRect,
    val transform: UiTransform,
    val event: (A, B) -> R
)

data class UiEventRegions3<A, B, C, R>(
    val rect: UiRect,
    val transform: UiTransform,
    val event: (A, B, C) -> R
)