package com.rjspies.daedalus.domain

import kotlinx.coroutines.flow.Flow

class GetSnackbarQueueUseCase(private val snackbarService: SnackbarService) {
    operator fun invoke(): Flow<SnackbarData> = snackbarService.snackbarQueue
}