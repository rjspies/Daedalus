package com.rjspies.daedalus.data

import android.content.Context
import androidx.compose.ui.util.fastForEach
import androidx.core.net.toUri
import com.rjspies.daedalus.domain.WEIGHT_COLUMN_IDENTIFIER_DATE_TIME
import com.rjspies.daedalus.domain.WEIGHT_COLUMN_IDENTIFIER_ID
import com.rjspies.daedalus.domain.WEIGHT_COLUMN_IDENTIFIER_NOTE
import com.rjspies.daedalus.domain.WEIGHT_COLUMN_IDENTIFIER_VALUE
import com.rjspies.daedalus.domain.Weight
import com.rjspies.daedalus.domain.WeightService
import java.io.OutputStreamWriter
import java.time.ZonedDateTime
import kotlinx.coroutines.flow.Flow

class WeightServiceImpl(
    private val weightDao: WeightDao,
    private val applicationContext: Context,
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

    override suspend fun deleteWeight(weight: Weight) = weightDao.deleteWeight(weight.id)
    override fun weightsDescending(): Flow<List<Weight>> = weightDao.weightsDescending()
    override fun weightsAscending(): Flow<List<Weight>> = weightDao.weightsAscending()
    override suspend fun exportWeights(path: String) {
        val outputStream = applicationContext.contentResolver.openOutputStream(path.toUri())
        OutputStreamWriter(outputStream).use {
            val allWeights = weightDao.getAllWeights()
            val headline = "$WEIGHT_COLUMN_IDENTIFIER_ID,$WEIGHT_COLUMN_IDENTIFIER_VALUE,$WEIGHT_COLUMN_IDENTIFIER_NOTE,$WEIGHT_COLUMN_IDENTIFIER_DATE_TIME"

            it.appendLine(headline)
            allWeights.fastForEach { weight ->
                it.appendLine("${weight.id},${weight.value},${weight.note},${weight.dateTime}")
            }
        }
    }
}
