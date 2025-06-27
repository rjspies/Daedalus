package com.rjspies.daedalus.presentation.common

import android.os.Parcelable
import java.time.ZonedDateTime
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeightChartEntry(
    val x: Float,
    val y: Float,
    val dateTime: ZonedDateTime,
) : Parcelable
