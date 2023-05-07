package com.gmail.uli153.workdaymeter.utils

import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

object Formatters {
    val dateTime        get() = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.getDefault())
    val date            get() = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    val dateTimeHuman   get() = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss", Locale.getDefault())

}