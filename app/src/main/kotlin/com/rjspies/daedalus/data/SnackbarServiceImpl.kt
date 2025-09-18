package com.rjspies.daedalus.data

import com.rjspies.daedalus.domain.SnackbarData
import com.rjspies.daedalus.domain.SnackbarService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class SnackbarServiceImpl : SnackbarService {
    private val _snackbarQueue: MutableSharedFlow<SnackbarData> = MutableSharedFlow(1)
    override val snackbarQueue: Flow<SnackbarData>
        get() = _snackbarQueue

    override suspend fun requestSnackbar(snackbarData: SnackbarData) {
        _snackbarQueue.emit(snackbarData)
    }
}