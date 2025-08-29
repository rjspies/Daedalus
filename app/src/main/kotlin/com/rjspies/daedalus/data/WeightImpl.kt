package com.rjspies.daedalus.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.rjspies.daedalus.domain.Weight
import java.time.ZonedDateTime

@Entity(tableName = "Weight")
@TypeConverters(ZonedDateTimeConverter::class)
data class WeightImpl(
    @PrimaryKey(autoGenerate = true)
    override val id: Int = 0,
    override val value: Float,
    override val note: String?,
    override val dateTime: ZonedDateTime,
) : Weight
