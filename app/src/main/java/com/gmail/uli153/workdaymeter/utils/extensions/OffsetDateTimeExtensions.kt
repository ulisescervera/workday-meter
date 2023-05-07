package com.gmail.uli153.workdaymeter.utils.extensions

import org.threeten.bp.OffsetDateTime

val OffsetDateTime.firstSecond: OffsetDateTime get() {
    return this.withHour(0).withMinute(0).withSecond(0)
}

val OffsetDateTime.lastSecond: OffsetDateTime get() {
    return this.withHour(23).withMinute(59).withSecond(59)
}