package com.gmail.uli153.workdaymeter.data

import com.gmail.uli153.workdaymeter.data.entities.RecordEntity
import kotlinx.coroutines.flow.Flow

interface WorkdayRepository {
    fun getStateFlow(): Flow<RecordEntity>
    fun getRecords(): Flow<List<RecordEntity>>
    suspend fun getState(): RecordEntity
    suspend fun insert(record: RecordEntity)
    suspend fun delete(record: RecordEntity)
}