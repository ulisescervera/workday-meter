package com.gmail.uli153.workdaymeter.domain.use_cases

import com.gmail.uli153.workdaymeter.data.WorkdayRepository
import com.gmail.uli153.workdaymeter.data.entities.ClockState
import com.gmail.uli153.workdaymeter.data.entities.RecordEntity
import java.util.Date

class ToggleStateUseCase(private val repository: WorkdayRepository) {

    suspend operator fun invoke() {
        val now = Date()
        val current = repository.getState()
        when(current.state) {
            ClockState.ClockIn -> repository.insert(RecordEntity(now, ClockState.ClockOut))
            ClockState.ClockOut -> repository.insert(RecordEntity(now, ClockState.ClockIn))
        }
    }
}