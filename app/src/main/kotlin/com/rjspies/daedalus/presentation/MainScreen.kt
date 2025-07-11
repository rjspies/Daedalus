package com.rjspies.daedalus.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.adevinta.spark.components.scaffold.Scaffold
import com.rjspies.daedalus.presentation.navigation.Route
import com.rjspies.daedalus.presentation.navigation.navigationGraph
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

@Composable
fun MainScreen() {
    Scaffold { padding ->
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
    }
}

@Composable
private fun BoxScope.StatusBarBlur(scaffoldPadding: PaddingValues, hazeState: HazeState) {
    Blur(scaffoldPadding.calculateTopPadding(), hazeState, Modifier.align(Alignment.TopCenter))
}

@Composable
private fun BoxScope.NavigationBarBlur(scaffoldPadding: PaddingValues, hazeState: HazeState) {
    Blur(scaffoldPadding.calculateBottomPadding(), hazeState, Modifier.align(Alignment.BottomCenter))
}

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
private fun Blur(height: Dp, hazeState: HazeState, modifier: Modifier = Modifier) {
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
