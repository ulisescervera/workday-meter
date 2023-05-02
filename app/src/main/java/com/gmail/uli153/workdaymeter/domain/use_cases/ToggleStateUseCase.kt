package com.gmail.uli153.workdaymeter.domain.use_cases

import com.gmail.uli153.workdaymeter.data.WorkdayRepository
import com.gmail.uli153.workdaymeter.data.entities.ClockState
import com.gmail.uli153.workdaymeter.data.entities.RecordEntity
import java.time.LocalDateTime
import java.util.*

class ToggleStateUseCase(private val repository: WorkdayRepository) {

    suspend operator fun invoke() {
        val now = LocalDateTime.now()
        val current = repository.getState()
        when(current.state) {
            ClockState.ClockIn -> repository.insert(RecordEntity(now, ClockState.ClockOut))
            ClockState.ClockOut -> repository.insert(RecordEntity(now, ClockState.ClockIn))
        }
    }
}