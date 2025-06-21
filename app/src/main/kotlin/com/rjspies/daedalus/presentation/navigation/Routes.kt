package com.rjspies.daedalus.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Route {
    @Serializable
    data object Diagram : Route()

    @Serializable
    data object History : Route()
}
