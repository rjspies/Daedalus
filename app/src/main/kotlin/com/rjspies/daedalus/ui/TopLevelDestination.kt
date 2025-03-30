package com.rjspies.daedalus.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ramcosta.composedestinations.generated.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.WeightDiagramScreenDestination
import com.ramcosta.composedestinations.generated.destinations.WeightHistoryScreenDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.rjspies.daedalus.R

enum class TopLevelDestination(
    val destination: DirectionDestinationSpec,
    @DrawableRes val iconResourceId: Int,
    @StringRes val labelResourceId: Int,
) {
    Diagram(
        destination = WeightDiagramScreenDestination,
        iconResourceId = R.drawable.chart_line_fill,
        labelResourceId = R.string.navigation_bar_graph,
    ),
    History(
        destination = WeightHistoryScreenDestination,
        iconResourceId = R.drawable.list_bullets_fill,
        labelResourceId = R.string.navigation_bar_history,
    ),
    Settings(
        destination = SettingsScreenDestination,
        iconResourceId = R.drawable.gear_fine_fill,
        labelResourceId = R.string.navigation_bar_settings,
    ),
}
