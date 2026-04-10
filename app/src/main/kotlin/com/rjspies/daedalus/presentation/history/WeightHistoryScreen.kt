package com.rjspies.daedalus.presentation.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FormatListNumbered
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rjspies.daedalus.R
import com.rjspies.daedalus.domain.Weight
import com.rjspies.daedalus.presentation.common.EmptyScreen
import com.rjspies.daedalus.presentation.common.Spacings
import com.rjspies.daedalus.presentation.common.VerticalSpacerS
import com.rjspies.daedalus.presentation.common.VerticalSpacerXS
import com.rjspies.daedalus.presentation.common.horizontalSpacingM
import com.rjspies.daedalus.presentation.common.daedalusHazeEffect
import com.rjspies.daedalus.presentation.common.verticalSpacingXXL
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightHistoryScreen(
    onOpenDrawer: () -> Unit,
    viewModel: WeightHistoryViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val hazeState = remember { HazeState() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.navigation_top_bar_title_history)) },
                modifier = Modifier.daedalusHazeEffect(hazeState),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(
                            imageVector = Icons.Rounded.Menu,
                            contentDescription = stringResource(R.string.navigation_drawer_open_content_description),
                        )
                    }
                },
            )
        },
        content = { scaffoldPadding ->
            if (uiState.weights.isNotEmpty()) {
                Weights(
                    weights = uiState.weights,
                    scaffoldPadding = scaffoldPadding,
                    hazeState = hazeState,
                )
            } else {
                EmptyScreen(
                    painter = rememberVectorPainter(Icons.Rounded.FormatListNumbered),
                    contentDescription = stringResource(R.string.weight_history_empty_screen_content_description),
                    title = stringResource(R.string.weight_history_empty_screen_title),
                    subtitle = stringResource(R.string.weight_history_empty_screen_subtitle),
                    modifier = Modifier
                        .fillMaxSize()
                        .hazeSource(hazeState)
                        .verticalScroll(rememberScrollState())
                        .padding(scaffoldPadding)
                        .verticalSpacingXXL()
                        .horizontalSpacingM(),
                )
            }
        },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Weights(
    weights: List<Weight>,
    scaffoldPadding: PaddingValues,
    hazeState: HazeState,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .hazeSource(hazeState),
        contentPadding = PaddingValues(
            top = scaffoldPadding.calculateTopPadding(),
            end = Spacings.M,
            bottom = scaffoldPadding.calculateBottomPadding() + Spacings.XXL,
            start = Spacings.M,
        ),
        content = {
            item {
                Text(
                    text = stringResource(R.string.weight_history_title),
                    style = MaterialTheme.typography.displayMedium,
                )
                VerticalSpacerS()
            }

            items(
                items = weights,
                key = { it.id },
                itemContent = { weight ->
                    val index = weights.indexOf(weight)
                    val predecessor = remember(weights) {
                        weights.getOrNull(index + 1)
                    }

                    WeightHistoryItem(
                        weight = weight,
                        predecessorWeight = predecessor,
                    )

                    if (index != weights.lastIndex) {
                        VerticalSpacerXS()
                    }
                },
            )
        },
    )
}
