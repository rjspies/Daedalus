package com.rjspies.daedalus.domain

import java.time.ZonedDateTime
import kotlinx.coroutines.flow.Flow

interface WeightService {
    suspend fun insertWeight(
        value: Float,
        note: String?,
        dateTime: ZonedDateTime,
    )
    suspend fun deleteWeight(weight: Weight)
    suspend fun exportWeights(path: String)
    fun weightsDescending(): Flow<List<Weight>>
    fun weightsAscending(): Flow<List<Weight>>
}
