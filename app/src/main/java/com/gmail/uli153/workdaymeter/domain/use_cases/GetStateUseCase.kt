package com.gmail.uli153.workdaymeter.domain.use_cases

import com.gmail.uli153.workdaymeter.data.WorkdayRepository
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.domain.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetStateUseCase(private val repository: WorkdayRepository) {

    operator fun invoke(): Flow<Record> {
        return repository.getStateFlow().map { it.toModel() }
    }
}