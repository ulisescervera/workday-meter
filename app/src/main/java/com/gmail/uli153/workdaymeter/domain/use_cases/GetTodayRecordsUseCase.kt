package com.gmail.uli153.workdaymeter.domain.use_cases

import com.gmail.uli153.workdaymeter.data.WorkdayRepository
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.domain.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest

class GetTodayRecordsUseCase(private val repository: WorkdayRepository) {

    operator fun invoke(): Flow<List<Record>> {
        return repository.getTodayRecords().transformLatest { it.map { it.toModel() } }
    }
}