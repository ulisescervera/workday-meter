package com.gmail.uli153.workdaymeter.utils

import java.text.SimpleDateFormat
import java.util.*

sealed class Formatters {
    object DateTimeFormatter: SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.getDefault())
    object DateFormatter: SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
}