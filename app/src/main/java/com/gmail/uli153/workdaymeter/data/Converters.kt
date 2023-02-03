package com.gmail.uli153.workdaymeter.data

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class Converters {

    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.getDefault())

    @TypeConverter
    fun toDate(timestamp: String): Date {
        return dateFormatter.parse(timestamp)!!
    }

    @TypeConverter
    fun toTimestamp(date: Date): String {
        return dateFormatter.format(date)
    }
}