package com.gmail.uli153.workdaymeter.data

import androidx.room.TypeConverter
import com.gmail.uli153.workdaymeter.data.entities.ClockState
import com.gmail.uli153.workdaymeter.utils.Formatters
import org.threeten.bp.OffsetDateTime

class Converters {

    @TypeConverter
    fun toDate(timestamp: String): OffsetDateTime {
        return Formatters.dateTime.parse(timestamp, OffsetDateTime::from)
    }

    @TypeConverter
    fun toTimestamp(date: OffsetDateTime): String {
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