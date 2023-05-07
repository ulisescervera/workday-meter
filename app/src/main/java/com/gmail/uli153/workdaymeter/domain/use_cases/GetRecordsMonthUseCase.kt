package com.gmail.uli153.workdaymeter.domain.use_cases

import com.gmail.uli153.workdaymeter.data.WorkdayRepository
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.domain.toModel
import com.gmail.uli153.workdaymeter.utils.extensions.firstSecond
import com.gmail.uli153.workdaymeter.utils.extensions.lastSecond
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.YearMonth


class GetRecordsMonthUseCase(private val repository: WorkdayRepository) {

    operator fun invoke(): Flow<List<Record>> {
        val from = OffsetDateTime.now()
            .withDayOfMonth(1)
            .firstSecond
        val to = OffsetDateTime.now()
            .withDayOfMonth(YearMonth.now().lengthOfMonth())
            .lastSecond
        return repository.getRecordsInRange(from, to).map { it.map { it.toModel() } }
    }
}