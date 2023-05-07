package com.gmail.uli153.workdaymeter.utils.extensions

import org.threeten.bp.OffsetDateTime
import org.threeten.bp.temporal.ChronoUnit

fun OffsetDateTime.millisSince(date: OffsetDateTime): Long {
    return ChronoUnit.MILLIS.between(date, this)
}