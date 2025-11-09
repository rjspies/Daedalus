package com.rjspies.daedalus.data

import com.rjspies.daedalus.domain.SnackbarRepository
import com.rjspies.daedalus.domain.SnackbarVisuals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class SnackbarRepositoryImpl : SnackbarRepository {
    private val _snackbarVisuals: MutableSharedFlow<SnackbarVisuals?> = MutableSharedFlow(1)
    override val snackbarVisuals: Flow<SnackbarVisuals?>
        get() = _snackbarVisuals

    override suspend fun showSnackbar(visuals: SnackbarVisuals) {
        _snackbarVisuals.emit(visuals)
    }
}
