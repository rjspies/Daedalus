package com.rjspies.daedalus.presentation.engineeringmenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.rjspies.daedalus.R
import com.rjspies.daedalus.presentation.common.HorizontalSpacerM
import com.rjspies.daedalus.presentation.common.OverviewScreenContent
import com.rjspies.daedalus.presentation.common.Spacings
import com.rjspies.daedalus.presentation.common.horizontalSpacingM
import com.rjspies.daedalus.presentation.common.horizontalSpacingS
import com.rjspies.daedalus.presentation.common.verticalSpacingL
import com.rjspies.daedalus.presentation.navigation.Route

@Composable
fun EngineeringMenuScreen(
    onOpenDrawer: () -> Unit,
    onNavigateTo: (Route) -> Unit,
) {
    OverviewScreenContent(
        title = stringResource(R.string.navigation_top_bar_title_engineering_menu),
        onOpenDrawer = onOpenDrawer,
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(scaffoldPadding),
            verticalArrangement = Arrangement.spacedBy(Spacings.XS),
        ) {
            Entry(stringResource(R.string.engineering_menu_item_typography_label)) { onNavigateTo(Route.EngineeringMenuTypography) }
            Entry(stringResource(R.string.engineering_menu_item_colors_label)) { onNavigateTo(Route.EngineeringMenuColors) }
        }
    }
}

@Composable
fun Entry(
    label: String,
    onClick: () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalSpacingM(),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalSpacingS()
                .verticalSpacingL(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            HorizontalSpacerM()
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
            )
        }
    }
}
