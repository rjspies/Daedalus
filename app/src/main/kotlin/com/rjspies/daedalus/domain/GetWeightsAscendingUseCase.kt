package com.rjspies.daedalus.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWeightsAscendingUseCase(private val service: WeightService) {
    operator fun invoke(): Flow<List<Weight>> = service.weightsAscending().map { it }
}
