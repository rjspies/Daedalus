package com.rjspies.daedalus.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rjspies.daedalus.presentation.diagram.WeightDiagramScreen
import com.rjspies.daedalus.presentation.history.WeightHistoryScreen

fun NavGraphBuilder.navigationGraph(padding: PaddingValues) {
    composable<Route.Diagram> {
        WeightDiagramScreen(scaffoldPadding = padding)
    }
    composable<Route.History> {
        WeightHistoryScreen(padding)
    }
}
