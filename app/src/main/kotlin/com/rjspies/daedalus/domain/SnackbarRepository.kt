package com.rjspies.daedalus.domain

import kotlinx.coroutines.flow.Flow

interface SnackbarRepository {
    val snackbarVisuals: Flow<SnackbarVisuals?>
    suspend fun showSnackbar(visuals: SnackbarVisuals)
}
