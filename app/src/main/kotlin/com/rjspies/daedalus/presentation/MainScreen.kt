package com.rjspies.daedalus.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ShowChart
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rjspies.daedalus.R
import com.rjspies.daedalus.domain.SnackbarVisuals
import com.rjspies.daedalus.presentation.common.Snackbar
import com.rjspies.daedalus.presentation.common.Spacings
import com.rjspies.daedalus.presentation.common.VerticalSpacerL
import com.rjspies.daedalus.presentation.common.VerticalSpacerXS
import com.rjspies.daedalus.presentation.navigation.Route
import com.rjspies.daedalus.presentation.navigation.navigationGraph
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navigationController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val currentBackStackEntry by navigationController.currentBackStackEntryAsState()

    val currentRoute = currentBackStackEntry?.run { destination.route }

    val isDiagramSelected = currentRoute?.contains(Route.Diagram::class.qualifiedName.orEmpty()) == true
    val isHistorySelected = currentRoute?.contains(Route.History::class.qualifiedName.orEmpty()) == true

    val hazeState = remember { HazeState() }
    val onOpenDrawer: () -> Unit = { coroutineScope.launch { drawerState.open() } }

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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                VerticalSpacerXS()
                Text(
                    text = stringResource(R.string.app_name),
                    modifier = Modifier.padding(horizontal = Spacings.M),
                )
                VerticalSpacerL()
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.navigation_drawer_item_diagram)) },
                    icon = { Icon(Icons.AutoMirrored.Rounded.ShowChart, contentDescription = null) },
                    selected = isDiagramSelected,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        navigationController.navigate(Route.Diagram) {
                            popUpTo(Route.Diagram) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.padding(horizontal = Spacings.M),
                )
                VerticalSpacerXS()
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.navigation_drawer_item_history)) },
                    icon = { Icon(Icons.Rounded.History, contentDescription = null) },
                    selected = isHistorySelected,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        navigationController.navigate(Route.History) {
                            popUpTo(Route.Diagram) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.padding(horizontal = Spacings.M),
                )
            }
        },
    ) {
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
                NavHost(
                    navController = navigationController,
                    startDestination = Route.Diagram,
                    builder = { navigationGraph(onOpenDrawer) },
                    modifier = Modifier.hazeSource(hazeState),
                )

                Box(Modifier.fillMaxSize()) {
                    NavigationBarBlur(padding, hazeState)
                }
            },
        )
    }
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
                blurRadius = 40.dp
                tints = emptyList()
                noiseFactor = 0f
                progressive = HazeProgressive.verticalGradient(
                    easing = LinearEasing,
                    startIntensity = .25f,
                    endIntensity = .25f,
                    endY = heightPx,
                )
            },
    )
}
