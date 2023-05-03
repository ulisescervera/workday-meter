package com.gmail.uli153.workdaymeter.data

import androidx.room.TypeConverter
import com.gmail.uli153.workdaymeter.data.entities.ClockState
import com.gmail.uli153.workdaymeter.utils.Formatters
import java.util.Date

class Converters {

    @TypeConverter
    fun toDate(timestamp: String): Date {
        return Formatters.dateTime.parse(timestamp)!!
    }

    @TypeConverter
    fun toTimestamp(date: Date): String {
        return Formatters.dateTime.format(date)
    }

    @TypeConverter
    fun toBoolean(state: ClockState): Boolean {
        return when(state) {
            ClockState.ClockIn -> true
            ClockState.ClockOut -> false
        }
    }

    @TypeConverter
    fun toState(bool: Boolean): ClockState {
        return if (bool) ClockState.ClockIn else ClockState.ClockOut
    }
}