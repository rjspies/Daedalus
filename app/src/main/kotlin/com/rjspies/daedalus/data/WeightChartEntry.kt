package com.rjspies.daedalus.data

import android.os.Parcelable
import java.time.ZonedDateTime
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class WeightChartEntry(
    val x: Float,
    val y: Float,
    val dateTime: ZonedDateTime,
) : Parcelable
