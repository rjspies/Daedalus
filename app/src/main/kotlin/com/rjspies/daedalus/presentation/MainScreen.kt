package com.rjspies.daedalus.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.rjspies.daedalus.domain.SnackbarVisuals
import com.rjspies.daedalus.presentation.common.Snackbar
import com.rjspies.daedalus.presentation.navigation.Route
import com.rjspies.daedalus.presentation.navigation.navigationGraph
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.snackbarVisuals) {
        val visuals = uiState.snackbarVisuals
        if (visuals != null) {
            val result = snackbarHostState.showSnackbar(
                visuals = SnackbarVisuals(
                    message = visuals.message,
                    actionLabel = visuals.actionLabel,
                    withDismissAction = visuals.withDismissAction,
                    duration = visuals.duration,
                    isError = visuals.isError,
                ),
            )
            when (result) {
                SnackbarResult.Dismissed,
                SnackbarResult.ActionPerformed,
                -> viewModel.onEvent(MainViewModel.Event.OnSnackbarDismissed)
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { snackbarData ->
                Snackbar(
                    message = snackbarData.visuals.message,
                    isError = (snackbarData.visuals as? SnackbarVisuals)?.isError == true,
                    onDismissRequest = snackbarData::dismiss,
                )
            }
        },
        content = { padding ->
            val hazeState = remember { HazeState() }
            val navigationController = rememberNavController()

            NavHost(
                navController = navigationController,
                startDestination = Route.Diagram,
                builder = { navigationGraph(navigationController, padding) },
                modifier = Modifier.hazeSource(hazeState),
            )

            Box(Modifier.fillMaxSize()) {
                StatusBarBlur(padding, hazeState)
                NavigationBarBlur(padding, hazeState)
            }
        },
    )
}

@Composable
private fun BoxScope.StatusBarBlur(
    scaffoldPadding: PaddingValues,
    hazeState: HazeState,
) {
    Blur(
        height = scaffoldPadding.calculateTopPadding(),
        hazeState = hazeState,
        modifier = Modifier.align(Alignment.TopCenter),
    )
}

@Composable
private fun BoxScope.NavigationBarBlur(
    scaffoldPadding: PaddingValues,
    hazeState: HazeState,
) {
    Blur(
        height = scaffoldPadding.calculateBottomPadding(),
        hazeState = hazeState,
        modifier = Modifier.align(Alignment.BottomCenter),
    )
}

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
private fun Blur(
    height: Dp,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
) {
    val heightPx = with(LocalDensity.current) { height.toPx() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .then(modifier)
            .hazeEffect(hazeState, HazeMaterials.regular()) {
                progressive = HazeProgressive.verticalGradient(
                    easing = LinearEasing,
                    startIntensity = .25f,
                    endIntensity = .25f,
                    endY = heightPx,
                )
            },
    )
}
