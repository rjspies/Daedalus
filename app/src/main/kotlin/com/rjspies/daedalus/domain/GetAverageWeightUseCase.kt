package com.rjspies.daedalus.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAverageWeightUseCase(
    private val weightService: WeightService
) {
    operator fun invoke(): Flow<Float> = weightService.weightsDescending().map { allWeights ->
        allWeights.groupBy { weight -> weight.dateTime.toLocalDate() }
            .mapValues { groupedWeights ->
                groupedWeights.value.map { weight -> weight.value }.average()
            }
            .values
            .average()
            .toFloat()
    }
}
