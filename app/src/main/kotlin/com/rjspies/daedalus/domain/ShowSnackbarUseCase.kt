package com.rjspies.daedalus.domain

class ShowSnackbarUseCase(private val repository: SnackbarRepository) {
    suspend operator fun invoke(visuals: SnackbarVisuals) = repository.showSnackbar(visuals)
}
