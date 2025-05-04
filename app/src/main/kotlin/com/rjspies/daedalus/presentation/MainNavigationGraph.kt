package com.rjspies.daedalus.presentation

import com.ramcosta.composedestinations.animations.defaults.DefaultFadingTransitions
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootGraph

@NavGraph<RootGraph>(start = true, defaultTransitions = DefaultFadingTransitions::class)
annotation class MainNavigationGraph(
    val start: Boolean = false,
)
