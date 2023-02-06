package com.gmail.uli153.workdaymeter.domain.use_cases

import com.gmail.uli153.workdaymeter.data.WorkdayRepository
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.domain.toEntity

class DeleteRecordUseCase(private val repository: WorkdayRepository) {

    suspend operator fun invoke(record: Record) {
        repository.delete(record.toEntity())
    }
}