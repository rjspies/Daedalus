package com.rjspies.daedalus.domain

import java.time.ZonedDateTime
import kotlinx.coroutines.flow.Flow

private const val THIRTY_DAY_PERIOD = 30L

class GetThirtyDayAverageWeightUseCase(private val service: WeightService) {
    operator fun invoke(): Flow<Float?> = service.averageWeightSince(ZonedDateTime.now().minusDays(THIRTY_DAY_PERIOD))
}
