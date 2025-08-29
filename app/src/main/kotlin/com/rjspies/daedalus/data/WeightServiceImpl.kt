package com.rjspies.daedalus.data

import com.rjspies.daedalus.domain.Weight
import com.rjspies.daedalus.domain.WeightService
import java.time.ZonedDateTime
import kotlinx.coroutines.flow.Flow

class WeightServiceImpl(
    private val weightDao: WeightDao,
) : WeightService {
    override suspend fun insertWeight(
        value: Float,
        note: String?,
        dateTime: ZonedDateTime,
    ) = weightDao.insert(
        weight = WeightImpl(
            value = value,
            note = note,
            dateTime = dateTime,
        ),
    )

    override fun weightsDescending(): Flow<List<Weight>> = weightDao.weightsDescending()
    override fun weightsAscending(): Flow<List<Weight>> = weightDao.weightsAscending()
    override suspend fun deleteWeight(weight: Weight) = weightDao.deleteWeight(weight.id)
}
