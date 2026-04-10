package com.rjspies.daedalus.presentation.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

@Composable
@OptIn(ExperimentalHazeMaterialsApi::class)
fun Modifier.daedalusHazeEffect(
    state: HazeState,
    endY: Float? = null,
): Modifier {
    val material = HazeMaterials.regular()
    return this then Modifier.hazeEffect(state, material) {
        blurRadius = 40.dp
        tints = emptyList()
        noiseFactor = 0f
        progressive = if (endY != null) {
            HazeProgressive.verticalGradient(
                easing = LinearEasing,
                startIntensity = .25f,
                endIntensity = .25f,
                endY = endY,
            )
        } else {
            HazeProgressive.verticalGradient(
                easing = LinearEasing,
                startIntensity = .25f,
                endIntensity = .25f,
            )
        }
    }
}
