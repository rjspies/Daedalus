package com.rjspies.daedalus.ui

import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.rememberDestinationsNavigator
import com.ramcosta.composedestinations.utils.startDestination
import com.rjspies.daedalus.IntentActions
import com.rjspies.daedalus.R
import com.rjspies.daedalus.ui.insertweight.AddWeightDialog
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel()) {
    val navigationController = rememberNavController()
    val navigator = navigationController.rememberDestinationsNavigator()
    val uiState by viewModel.uiState.collectAsState()
    val intent = LocalActivity.current?.intent

    IntentHandler(intent) {
        when (it) {
            IntentActions.InsertWeight -> viewModel.setShowDialog(true)
        }
    }

    if (uiState.showDialog) {
        AddWeightDialog {
            viewModel.setShowDialog(false)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.setShowDialog(true) },
                content = {
                    Icon(
                        painter = painterResource(R.drawable.plus_circle_fill),
                        contentDescription = stringResource(R.string.main_screen_floating_action_button_content_description),
                    )
                },
            )
        },
        bottomBar = {
            val currentDestination = navigationController.currentDestinationAsState().value ?: NavGraphs.root.startDestination
            NavigationBar(
                currentDestination = currentDestination,
                navigate = {
                    navigator.navigate(it) {
                        restoreState = true
                        launchSingleTop = true
                        popUpTo(NavGraphs.root.startRoute) {
                            saveState = true
                        }
                    }
                },
            )
        },
        content = {
            val hazeState = remember { HazeState() }

            DestinationsNavHost(
                navGraph = NavGraphs.root,
                modifier = Modifier.hazeSource(hazeState),
                navController = navigationController,
                dependenciesContainerBuilder = {
                    dependency(it)
                    dependency(navigator)
                    dependency(hazeState)
                },
                engine = rememberNavHostEngine(),
            )

            StatusBarBlur(it, hazeState)
        },
    )
}

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
private fun StatusBarBlur(scaffoldPadding: PaddingValues, hazeState: HazeState) {
    val height = with(LocalDensity.current) { scaffoldPadding.calculateTopPadding().toPx() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(scaffoldPadding.calculateTopPadding())
            .hazeEffect(hazeState, HazeMaterials.regular()) {
                progressive = HazeProgressive.verticalGradient(
                    easing = LinearEasing,
                    startIntensity = 1f,
                    endIntensity = 0f,
                    endY = height,
                )
            },
    )
}

@Composable
private fun IntentHandler(intent: Intent?, onIntentReceived: (IntentActions) -> Unit) {
    LaunchedEffect(intent) {
        when (intent?.action) {
            IntentActions.InsertWeight.action -> onIntentReceived(IntentActions.InsertWeight)
        }
    }
}
