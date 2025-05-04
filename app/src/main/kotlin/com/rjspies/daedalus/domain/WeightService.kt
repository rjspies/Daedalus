package com.rjspies.daedalus.domain

import java.time.ZonedDateTime
import kotlinx.coroutines.flow.Flow

interface WeightService {
    suspend fun insertWeight(
        value: Float,
        note: String?,
        dateTime: ZonedDateTime,
    )

    fun weightsDescending(): Flow<List<Weight>>
    fun weightsAscending(): Flow<List<Weight>>
    suspend fun deleteWeight(weight: Weight)
}
