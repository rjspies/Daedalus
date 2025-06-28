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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rjspies.daedalus.R
import com.rjspies.daedalus.domain.Weight
import com.rjspies.daedalus.presentation.common.EmptyScreen
import com.rjspies.daedalus.presentation.common.Spacings
import com.rjspies.daedalus.presentation.common.VerticalSpacerS
import com.rjspies.daedalus.presentation.common.VerticalSpacerXS
import com.rjspies.daedalus.presentation.common.horizontalSpacingM
import com.rjspies.daedalus.presentation.common.verticalSpacingM
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeightHistoryScreen(
    scaffoldPadding: PaddingValues,
    viewModel: WeightHistoryViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.weights.isNotEmpty()) {
        Weights(
            weights = uiState.weights,
            scaffoldPadding = scaffoldPadding,
        )
    } else {
        EmptyScreen(
            painter = rememberVectorPainter(Icons.Rounded.FormatListNumbered),
            contentDescription = stringResource(R.string.weight_history_empty_screen_content_description),
            title = stringResource(R.string.weight_history_empty_screen_title),
            subtitle = stringResource(R.string.weight_history_empty_screen_subtitle),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(scaffoldPadding)
                .verticalSpacingM()
                .horizontalSpacingM(),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Weights(
    weights: List<Weight>,
    scaffoldPadding: PaddingValues,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = scaffoldPadding.calculateTopPadding(),
            end = Spacings.M,
            bottom = scaffoldPadding.calculateBottomPadding(),
            start = Spacings.M,
        ),
        content = {
            item {
                Text(
                    text = stringResource(R.string.weight_history_title),
                    style = MaterialTheme.typography.headlineMedium,
                )
                VerticalSpacerS()
            }

            items(
                items = weights,
                key = { it.id },
                itemContent = {
                    val index = weights.indexOf(it)
                    val predecessor = remember(weights) {
                        weights.getOrNull(index + 1)
                    }

                    WeightHistoryItem(
                        weight = it,
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
