package com.rjspies.daedalus.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.rjspies.daedalus.domain.WEIGHT_COLUMN_IDENTIFIER_DATE_TIME
import com.rjspies.daedalus.domain.WEIGHT_COLUMN_IDENTIFIER_ID
import com.rjspies.daedalus.domain.WEIGHT_COLUMN_IDENTIFIER_NOTE
import com.rjspies.daedalus.domain.WEIGHT_COLUMN_IDENTIFIER_VALUE
import com.rjspies.daedalus.domain.WEIGHT_TABLE_IDENTIFIER
import com.rjspies.daedalus.domain.Weight
import java.time.ZonedDateTime

@Entity(tableName = WEIGHT_TABLE_IDENTIFIER)
@TypeConverters(ZonedDateTimeConverter::class)
data class WeightImpl(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = WEIGHT_COLUMN_IDENTIFIER_ID)
    override val id: Int = 0,
    @ColumnInfo(name = WEIGHT_COLUMN_IDENTIFIER_VALUE)
    override val value: Float,
    @ColumnInfo(name = WEIGHT_COLUMN_IDENTIFIER_NOTE)
    override val note: String?,
    @ColumnInfo(name = WEIGHT_COLUMN_IDENTIFIER_DATE_TIME)
    override val dateTime: ZonedDateTime,
) : Weight
