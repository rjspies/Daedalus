package com.rjspies.daedalus.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rjspies.daedalus.presentation.diagram.WeightDiagramScreen
import com.rjspies.daedalus.presentation.engineeringmenu.EngineeringMenuScreen
import com.rjspies.daedalus.presentation.engineeringmenu.colors.ColorsScreen
import com.rjspies.daedalus.presentation.engineeringmenu.typography.TypographyScreen
import com.rjspies.daedalus.presentation.history.WeightHistoryScreen

fun NavGraphBuilder.navigationGraph(
    onOpenDrawer: () -> Unit,
    onNavigateUp: () -> Unit,
    onNavigateTo: (Route) -> Unit,
) {
    composable<Route.Diagram> {
        WeightDiagramScreen(onOpenDrawer = onOpenDrawer)
    }
    composable<Route.History> {
        WeightHistoryScreen(onOpenDrawer = onOpenDrawer)
    }
    composable<Route.EngineeringMenu> {
        EngineeringMenuScreen(
            onOpenDrawer = onOpenDrawer,
            onNavigateTo = onNavigateTo,
        )
    }
    composable<Route.EngineeringMenuTypography> {
        TypographyScreen(onNavigateUp)
    }
    composable<Route.EngineeringMenuColors> {
        ColorsScreen(onNavigateUp)
    }
}
