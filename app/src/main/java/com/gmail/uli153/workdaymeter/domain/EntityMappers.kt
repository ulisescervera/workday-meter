package com.gmail.uli153.workdaymeter.domain

import android.util.Log
import com.gmail.uli153.workdaymeter.data.entities.ClockState
import com.gmail.uli153.workdaymeter.data.entities.RecordEntity
import com.gmail.uli153.workdaymeter.domain.models.MeterState
import com.gmail.uli153.workdaymeter.domain.models.Record
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

fun RecordEntity.toModel(): Record {
    return Record(this.timestamp, when(this.state) {
        ClockState.ClockIn -> MeterState.StateIn
        ClockState.ClockOut -> MeterState.StateOut
    })
}

fun Record.toEntity(): RecordEntity {
    return RecordEntity(this.date, when(this.state) {
        MeterState.StateIn -> ClockState.ClockIn
        MeterState.StateOut -> ClockState.ClockOut
    })
}