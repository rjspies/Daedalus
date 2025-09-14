package com.rjspies.daedalus.domain

class ExportWeightsUseCase(private val weightService: WeightService) {
    suspend operator fun invoke(path: String?) {
        path?.let { weightService.exportWeights(it) }
    }
}
