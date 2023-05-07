package com.gmail.uli153.workdaymeter.domain.models

import org.threeten.bp.OffsetDateTime

data class Record(
    val date: OffsetDateTime,
    val state: MeterState
)

enum class MeterState {
    StateIn, StateOut
}