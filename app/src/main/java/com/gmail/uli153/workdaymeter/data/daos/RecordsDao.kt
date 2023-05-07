package com.gmail.uli153.workdaymeter.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.gmail.uli153.workdaymeter.data.entities.RecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordsDao {

    @Query("SELECT * FROM records ORDER BY timestamp DESC LIMIT 1")
    fun getStateFlow(): Flow<List<RecordEntity>>

    @Query("SELECT * FROM records ORDER BY timestamp DESC LIMIT 1")
    fun getState(): List<RecordEntity>

    @Query("SELECT * FROM records ORDER BY timestamp")
    fun getRecords(): Flow<List<RecordEntity>>

    @Query("SELECT * FROM records WHERE timestamp BETWEEN :from AND :to ORDER BY datetime(timestamp)")
    fun getRecordsInRange(from: String, to: String): Flow<List<RecordEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insert(record: RecordEntity)

    @Delete
    suspend fun delete(record: RecordEntity)

    @Query("SELECT * FROM records WHERE timestamp BETWEEN :from AND :to ORDER BY datetime(timestamp)")
    fun getTest(from: String, to: String): List<RecordEntity>
}