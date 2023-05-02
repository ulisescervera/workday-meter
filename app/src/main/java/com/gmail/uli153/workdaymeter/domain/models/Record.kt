package com.gmail.uli153.workdaymeter.domain.models

import java.time.LocalDateTime

data class Record(
    val date: LocalDateTime,
    val state: MeterState
)

enum class MeterState {
    StateIn, StateOut
}