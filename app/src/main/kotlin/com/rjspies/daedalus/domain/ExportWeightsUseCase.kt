package com.rjspies.daedalus.domain

class ExportWeightsUseCase(private val weightService: WeightService) {
    suspend operator fun invoke(path: String?) {
        if (!path.isNullOrBlank()) weightService.exportWeights(path)
    }
}
