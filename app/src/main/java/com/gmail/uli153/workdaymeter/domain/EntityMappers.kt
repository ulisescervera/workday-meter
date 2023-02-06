package com.gmail.uli153.workdaymeter.domain

import com.gmail.uli153.workdaymeter.data.entities.ClockState
import com.gmail.uli153.workdaymeter.data.entities.RecordEntity
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.domain.models.State

fun RecordEntity.toModel(): Record {
    return Record(this.timestamp, when(this.state) {
        ClockState.ClockIn -> State.StateIn
        ClockState.ClockOut -> State.StateOut
    })
}

fun Record.toEntity(): RecordEntity {
    return RecordEntity(this.date, when(this.state) {
        State.StateIn -> ClockState.ClockIn
        State.StateOut -> ClockState.ClockOut
    })
}