package com.rjspies.daedalus.presentation

import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
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

private val RADIAL_GRADIENT_COLOR_1 = Color(0xFF302B63)
private val RADIAL_GRADIENT_COLOR_2 = Color(0xFF24243E)
private val RADIAL_GRADIENT_COLOR_3 = Color(0xFF0F0C29)
private const val RADIAL_GRADIENT_POSITION_OFFSET = .3f
private const val RADIAL_GRADIENT_RADIUS_OFFSET = 1.7f

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
        containerColor = Color.Transparent,
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
            GradientBackground()

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

@Composable
private fun GradientBackground() {
    val window = LocalWindowInfo.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        RADIAL_GRADIENT_COLOR_1,
                        RADIAL_GRADIENT_COLOR_2,
                        RADIAL_GRADIENT_COLOR_3,
                    ),
                    center = Offset(
                        x = window.containerSize.height * RADIAL_GRADIENT_POSITION_OFFSET,
                        y = window.containerSize.width * RADIAL_GRADIENT_POSITION_OFFSET,
                    ),
                    radius = window.containerSize.width * RADIAL_GRADIENT_RADIUS_OFFSET,
                ),
            ),
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
            .hazeEffect(hazeState, HazeMaterials.regular(Color.Transparent)) {
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
