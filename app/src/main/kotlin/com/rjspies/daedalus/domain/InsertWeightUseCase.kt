package com.rjspies.daedalus.domain

import java.time.ZonedDateTime

class InsertWeightUseCase(private val service: WeightService) {
    suspend operator fun invoke(
        value: Float,
        note: String?,
        dateTime: ZonedDateTime,
    ): Unit = service.insertWeight(
        value = value,
        note = note,
        dateTime = dateTime,
    )
}
