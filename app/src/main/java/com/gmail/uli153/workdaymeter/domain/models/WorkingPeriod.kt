package com.gmail.uli153.workdaymeter.domain.models

import java.util.Date

/**
 * Created by Ulises on 4/5/23.
 */
data class WorkingPeriod(
    val start: Date,
    val end: Date?
)
