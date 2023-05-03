package com.gmail.uli153.workdaymeter.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "records")
data class RecordEntity(
    @PrimaryKey                 val timestamp: Date,
    @ColumnInfo(name = "state") val state: ClockState
)

enum class ClockState {
    ClockIn, ClockOut
}
