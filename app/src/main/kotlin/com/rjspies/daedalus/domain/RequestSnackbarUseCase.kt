package com.rjspies.daedalus.domain

class RequestSnackbarUseCase(private val service: SnackbarService) {
    suspend operator fun invoke(snackbarData: SnackbarData) = service.requestSnackbar(snackbarData)
}