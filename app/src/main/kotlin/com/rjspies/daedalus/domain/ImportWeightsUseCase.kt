package com.rjspies.daedalus.domain

class ImportWeightsUseCase(private val weightService: WeightService) {
    suspend operator fun invoke(path: String?) {
        if (!path.isNullOrBlank()) weightService.importWeights(path)
    }
}
