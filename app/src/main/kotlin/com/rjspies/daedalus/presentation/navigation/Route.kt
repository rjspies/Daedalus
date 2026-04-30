package com.rjspies.daedalus.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Route {
    @Serializable
    data object Diagram : Route()

    @Serializable
    data object History : Route()

    @Serializable
    data object EngineeringMenu : Route()

    @Serializable
    data object EngineeringMenuTypography : Route()

    @Serializable
    data object EngineeringMenuColors : Route()
}
