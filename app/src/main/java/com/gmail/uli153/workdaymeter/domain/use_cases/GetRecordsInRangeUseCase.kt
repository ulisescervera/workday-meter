package com.gmail.uli153.workdaymeter.domain.use_cases

import com.gmail.uli153.workdaymeter.data.WorkdayRepository
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.domain.toModel
import com.gmail.uli153.workdaymeter.utils.extensions.firstSecond
import com.gmail.uli153.workdaymeter.utils.extensions.lastSecond
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.OffsetDateTime

class GetRecordsInRangeUseCase(private val repository: WorkdayRepository) {

    operator fun invoke(from: OffsetDateTime, to: OffsetDateTime): Flow<List<Record>> {
        val f = from.firstSecond
        val t = to.lastSecond
        return repository.getRecordsInRange(f, t).map { it.map { it.toModel() } }
    }
}