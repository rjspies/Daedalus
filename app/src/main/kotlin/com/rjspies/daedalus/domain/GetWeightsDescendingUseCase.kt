package com.rjspies.daedalus.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWeightsDescendingUseCase(private val service: WeightService) {
    operator fun invoke(): Flow<List<Weight>> = service.weightsDescending().map { it }
}
