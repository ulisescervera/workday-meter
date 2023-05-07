package com.gmail.uli153.workdaymeter.domain.models

import com.gmail.uli153.workdaymeter.utils.extensions.millisSince
import org.threeten.bp.OffsetDateTime

/**
 * Created by Ulises on 4/5/23.
 */
data class WorkingPeriod(
    val start: OffsetDateTime,
    val end: OffsetDateTime
) {
    val duration: Long get() = end.millisSince(start)
}
