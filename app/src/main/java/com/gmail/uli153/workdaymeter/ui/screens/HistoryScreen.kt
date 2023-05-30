package com.gmail.uli153.workdaymeter.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmail.uli153.workdaymeter.R
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.MeterState
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.domain.models.WorkingPeriod
import com.gmail.uli153.workdaymeter.ui.viewmodel.HistoryFilter
import com.gmail.uli153.workdaymeter.ui.views.DayFilterSelector
import com.gmail.uli153.workdaymeter.ui.views.HistoryDropdownMenu
import com.gmail.uli153.workdaymeter.utils.AppDimens
import com.gmail.uli153.workdaymeter.utils.Formatters
import com.gmail.uli153.workdaymeter.utils.extensions.formattedTime
import com.gmail.uli153.workdaymeter.utils.mockWorkingPeriods
import org.threeten.bp.DayOfWeek
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun HistoryScreen(
    padding: PaddingValues,
    state: State<UIState<Record>>,
    filter: State<HistoryFilter>,
    history: State<UIState<List<WorkingPeriod>>>,
    time: State<Long>,
    selectedDays: State<Set<DayOfWeek>>,
    filterSelectedListener: (HistoryFilter) -> Unit,
    onDayListChanged: (Set<DayOfWeek>) -> Unit
) {
    val items: List<WorkingPeriod> = when(val p = history.value) {
        is UIState.Loading -> emptyList()
        is UIState.Success -> p.data
    }
    val formatter = Formatters.dateTimeHuman
    val stateDate = (state.value as? UIState.Success)?.data?.let {
        it.takeIf { it.state == MeterState.StateIn }?.date
    }
    val listPadding = PaddingValues(
        start = 20.dp,
        top = 20.dp,
        end = 20.dp,
        bottom = padding.calculateBottomPadding() + 20.dp
    )
    LazyColumn(
        contentPadding = listPadding,
        modifier = Modifier
            .fillMaxSize(1f)
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            HistoryDropdownMenu(filter, filterSelectedListener)
            DayFilterSelector(selectedDays, Modifier.fillMaxWidth(), onDayListChanged)
            Spacer(modifier = Modifier.height(AppDimens.rowVSpace))
        }

        if (stateDate != null) {
            item {
                WorkingPeriodBaseRow(formatter, stateDate, null, time.value)
                Spacer(modifier = Modifier.height(AppDimens.rowVSpace))
            }
        }
        items(items) {
            WorkingPeriodRow(formatter, it)
            Spacer(modifier = Modifier.height(AppDimens.rowVSpace))
        }
    }
}

@Composable
private fun WorkingPeriodBaseRow(
    formatter: DateTimeFormatter,
    start: OffsetDateTime,
    end: OffsetDateTime?,
    duration: Long
){
    val endText = end?.let { formatter.format(end) } ?: stringResource(R.string.waiting_clock_out)

    ElevatedCard(shape = RoundedCornerShape(AppDimens.rowCornerRadius),
        modifier = Modifier.fillMaxWidth(1f)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = AppDimens.rowHPadding, vertical = AppDimens.rowVPadding)
        ) {
            Column {
                Text(text = formatter.format(start))
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = endText)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = duration.formattedTime)
        }
    }
}

@Composable
private fun WorkingPeriodRow(formatter: DateTimeFormatter, period: WorkingPeriod) {
    WorkingPeriodBaseRow(formatter, period.start, period.end, period.duration)
}

@Preview
@Composable
fun HistoryScreen_Preview() {
    val history = mockWorkingPeriods
    val time = remember { mutableStateOf(10L) }
    val state = remember { mutableStateOf(UIState.Success(Record(OffsetDateTime.now(), MeterState.StateIn))) }
    val filter = remember { mutableStateOf(HistoryFilter.All) }
    val days = remember { mutableStateOf(DayOfWeek.values().toSet()) }
    HistoryScreen(PaddingValues(0.dp), state, filter, history, time, days, {}, {})
}