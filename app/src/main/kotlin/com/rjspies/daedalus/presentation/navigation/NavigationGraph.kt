package com.rjspies.daedalus.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rjspies.daedalus.presentation.diagram.WeightDiagramScreen
import com.rjspies.daedalus.presentation.history.WeightHistoryScreen

fun NavGraphBuilder.navigationGraph(onOpenDrawer: () -> Unit) {
    composable<Route.Diagram> {
        WeightDiagramScreen(onOpenDrawer = onOpenDrawer)
    }
    composable<Route.History> {
        WeightHistoryScreen(onOpenDrawer = onOpenDrawer)
    }
}
