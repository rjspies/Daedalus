package com.rjspies.daedalus.domain

class GetSnackbarUseCase(private val repository: SnackbarRepository) {
    operator fun invoke() = repository.snackbarVisuals
}
