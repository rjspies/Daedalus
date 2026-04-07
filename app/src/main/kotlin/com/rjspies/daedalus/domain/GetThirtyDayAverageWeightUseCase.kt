package com.rjspies.daedalus.domain

import kotlinx.coroutines.flow.Flow

class GetThirtyDayAverageWeightUseCase(private val service: WeightService) {
    operator fun invoke(): Flow<Float?> = service.thirtyDayAverageWeight()
}
