package com.rjspies.daedalus.presentation

import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.utils.rememberDestinationsNavigator
import com.rjspies.daedalus.IntentActions
import com.rjspies.daedalus.R
import com.rjspies.daedalus.presentation.insertweight.InsertWeightDialog
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
        InsertWeightDialog {
            viewModel.setShowDialog(false)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.setShowDialog(true) },
                content = {
                    Icon(
                        painter = rememberVectorPainter(Icons.Rounded.Add),
                        contentDescription = stringResource(R.string.main_screen_floating_action_button_content_description),
                    )
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

            Box(Modifier.fillMaxSize()) {
                StatusBarBlur(it, hazeState)
                NavigationBarBlur(it, hazeState)
            }
        },
    )
}

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
private fun BoxScope.StatusBarBlur(scaffoldPadding: PaddingValues, hazeState: HazeState) {
    val height = with(LocalDensity.current) { scaffoldPadding.calculateTopPadding().toPx() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter)
            .height(scaffoldPadding.calculateTopPadding())
            .hazeEffect(hazeState, HazeMaterials.regular()) {
                progressive = HazeProgressive.verticalGradient(
                    easing = LinearEasing,
                    startIntensity = .25f,
                    endIntensity = .25f,
                    endY = height,
                )
            },
    )
}

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
private fun BoxScope.NavigationBarBlur(scaffoldPadding: PaddingValues, hazeState: HazeState) {
    val height = with(LocalDensity.current) { scaffoldPadding.calculateBottomPadding().toPx() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .height(scaffoldPadding.calculateBottomPadding())
            .hazeEffect(hazeState, HazeMaterials.regular()) {
                progressive = HazeProgressive.verticalGradient(
                    easing = LinearEasing,
                    startIntensity = .25f,
                    endIntensity = .25f,
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
