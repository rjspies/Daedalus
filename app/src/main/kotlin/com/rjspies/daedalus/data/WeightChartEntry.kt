package com.rjspies.daedalus.data

import java.time.ZonedDateTime

internal data class WeightChartEntry(
    val x: Float,
    val y: Float,
    val dateTime: ZonedDateTime,
)
