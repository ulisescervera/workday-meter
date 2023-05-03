package com.gmail.uli153.workdaymeter.utils

import java.text.SimpleDateFormat
import java.util.*

object Formatters {
    val dateTime    get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.getDefault())
    val date        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
}