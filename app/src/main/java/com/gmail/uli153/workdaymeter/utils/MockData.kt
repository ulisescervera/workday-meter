/**
 * Created by Ulises on 5/5/23.
 */
package com.gmail.uli153.workdaymeter.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.WorkingPeriod
import java.util.*

val mockWorkingPeriods: State<UIState<List<WorkingPeriod>>> get() {
    val l = mutableListOf<WorkingPeriod>()
    val numberOfDays = 20
    val oneHourMillis = 1000 * 60 * 60
    val oneDayMillis = 24 * oneHourMillis
    val start = Date(Date().time - numberOfDays * oneDayMillis)
    for (i in 1 until numberOfDays) {
        val days = i * oneDayMillis
        val s = Date(start.time + days)
        l.add(WorkingPeriod(start = s, end = Date(s.time + oneHourMillis)))
    }
    return mutableStateOf(UIState.Success(l))
}