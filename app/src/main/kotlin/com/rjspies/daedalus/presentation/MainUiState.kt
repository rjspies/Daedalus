package com.rjspies.daedalus.presentation

import com.rjspies.daedalus.presentation.common.UiState
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainUiState(
    val showDialog: Boolean = false,
) : UiState
