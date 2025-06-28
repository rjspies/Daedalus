package com.rjspies.daedalus.domain

class DeleteWeightUseCase(private val service: WeightService) {
    suspend operator fun invoke(weight: Weight): Unit = service.deleteWeight(weight)
}
