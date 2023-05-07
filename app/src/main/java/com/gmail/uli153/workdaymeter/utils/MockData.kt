/**
 * Created by Ulises on 5/5/23.
 */
package com.gmail.uli153.workdaymeter.utils

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.MeterState
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.domain.models.WorkingPeriod
import org.threeten.bp.OffsetDateTime

val mockWorkingPeriods: State<UIState<List<WorkingPeriod>>> get() {
    val l = mutableListOf<WorkingPeriod>()
    val numberOfDays: Long = 20
    for (i in 1 until numberOfDays) {
        val s = OffsetDateTime.now().minusDays(numberOfDays + i)
        val e = OffsetDateTime.now().minusDays(numberOfDays + i).plusHours(1)
        l.add(WorkingPeriod(start = s, end = e))
    }
    return mutableStateOf(UIState.Success(l))
}

val mockHistory: List<Record> get() {
    val l = mutableListOf<Record>()
    l.add(Record(OffsetDateTime.now().minusDays(400), MeterState.StateIn))
    l.add(Record(OffsetDateTime.now().minusDays(400).plusHours(1), MeterState.StateOut))
    l.add(Record(OffsetDateTime.now().minusMonths(2), MeterState.StateIn))
    l.add(Record(OffsetDateTime.now().minusMonths(2).plusHours(1), MeterState.StateOut))
    l.add(Record(OffsetDateTime.now().minusDays(2).withHour(0).withMinute(0).withSecond(0), MeterState.StateIn))
    l.add(Record(OffsetDateTime.now().minusDays(2).withHour(1).withMinute(0).withSecond(0), MeterState.StateOut))
    l.add(Record(OffsetDateTime.now().withHour(0).withMinute(0).withSecond(0), MeterState.StateIn))
    l.add(Record(OffsetDateTime.now().withHour(1).withMinute(0).withSecond(0), MeterState.StateOut))
    return l
}