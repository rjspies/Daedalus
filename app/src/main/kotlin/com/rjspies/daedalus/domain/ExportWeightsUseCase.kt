package com.rjspies.daedalus.domain

class ExportWeightsUseCase(private val weightService: WeightService) {
    suspend operator fun invoke(path: String?): Result<Unit> {
        return if (path.isNullOrBlank()) {
            Result.failure(IllegalArgumentException("URI is null or blank"))
        } else {
            runCatching { weightService.exportWeights(path) }
        }
    }
}
