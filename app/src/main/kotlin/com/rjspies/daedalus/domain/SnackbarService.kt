package com.rjspies.daedalus.domain

import kotlinx.coroutines.flow.Flow

interface SnackbarService {
    val snackbarQueue: Flow<SnackbarData>
    suspend fun requestSnackbar(snackbarData: SnackbarData)
}