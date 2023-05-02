package com.gmail.uli153.workdaymeter.utils

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

object Formatters {
    val dateTime get() = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.getDefault())
    val date get() = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
}