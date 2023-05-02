package com.gmail.uli153.workdaymeter.data

import android.util.Log
import androidx.room.TypeConverter
import com.gmail.uli153.workdaymeter.data.entities.ClockState
import com.gmail.uli153.workdaymeter.utils.Formatters
import java.time.LocalDateTime
import java.util.*

class Converters {

    private val formatter = Formatters.dateTime

    @TypeConverter
    fun toDate(timestamp: String): LocalDateTime {
        Log.d("######", "Converting: $timestamp")
        return LocalDateTime.from(formatter.parse(timestamp))
    }

    @TypeConverter
    fun toTimestamp(date: LocalDateTime): String {
        return formatter.format(date)
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