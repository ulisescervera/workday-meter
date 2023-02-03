package com.gmail.uli153.workdaymeter.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.gmail.uli153.workdaymeter.data.entities.ClockInOutEntity

@Dao
interface ClockInOutDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(clock: ClockInOutEntity)

    @Delete
    suspend fun delete(clock: ClockInOutEntity)
}