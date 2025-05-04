package com.rjspies.daedalus.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightDao {
    @Upsert
    suspend fun insert(weight: WeightImpl)

    @Query("SELECT * FROM Weight ORDER BY dateTime DESC")
    fun weightsDescending(): Flow<List<WeightImpl>>

    @Query("SELECT * FROM Weight ORDER BY dateTime ASC")
    fun weightsAscending(): Flow<List<WeightImpl>>

    @Query("DELETE FROM Weight WHERE id = :id")
    suspend fun deleteWeight(id: Int)
}
