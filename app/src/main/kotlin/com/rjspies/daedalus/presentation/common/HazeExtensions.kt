package com.rjspies.daedalus.presentation.common

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
fun Modifier.daedalusHazeEffect(state: HazeState): Modifier {
    val material = HazeMaterials.regular()
    return this then Modifier.hazeEffect(state, material) {
        blurRadius = 20.dp
        tints = emptyList()
        noiseFactor = 0f
        // HazeEffectScope has no direct intensity property; intensity is only configurable
        // via HazeProgressive. Equal start and end values result in a constant intensity.
        progressive = HazeProgressive.verticalGradient(
            startIntensity = .5f,
            endIntensity = .5f,
        )
    }
}
