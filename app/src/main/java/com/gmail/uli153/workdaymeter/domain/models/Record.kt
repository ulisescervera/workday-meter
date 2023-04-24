package com.gmail.uli153.workdaymeter.domain.models

import java.util.*

data class Record(
    val date: Date,
    val state: State
)

enum class State {
    StateIn, StateOut
}