package com.gmail.uli153.workdaymeter.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gmail.uli153.workdaymeter.R
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.MeterState
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.domain.models.WorkingPeriod
import com.gmail.uli153.workdaymeter.navigation.NavigationItem
import com.gmail.uli153.workdaymeter.ui.viewmodel.DateFilter
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
    filter: State<DateFilter>,
    history: State<UIState<List<WorkingPeriod>>>,
    time: State<Long>,
    selectedDays: State<Set<DayOfWeek>>,
    isFilterVisible: State<Boolean>,
    navigateHomeListener: () -> Unit,
    filterSelectedListener: (DateFilter) -> Unit,
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
        top = padding.calculateTopPadding() + 20.dp,
        end = 20.dp,
        bottom = padding.calculateBottomPadding() + 20.dp
    )

    LazyColumn(
        contentPadding = listPadding,
        modifier = Modifier
            .fillMaxSize(1f)
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (isFilterVisible.value) {
            item {
                HistoryDropdownMenu(filter, filterSelectedListener)
                DayFilterSelector(selectedDays, Modifier.fillMaxWidth(), onDayListChanged)
                Spacer(modifier = Modifier.height(AppDimens.rowVSpace))
            }
        }

        if (stateDate != null) {
            item {
                WorkingPeriodBaseRow(formatter, stateDate, null, time.value, navigateHomeListener)
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
    duration: Long,
    onClick: () -> Unit
){
    val endText = end?.let { formatter.format(end) } ?: stringResource(R.string.waiting_clock_out)

    ElevatedCard(shape = RoundedCornerShape(AppDimens.rowCornerRadius),
        modifier = Modifier.fillMaxWidth(1f).clickable(onClick = onClick)
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
    WorkingPeriodBaseRow(formatter, period.start, period.end, period.duration, {})
}

@Preview
@Composable
fun HistoryScreen_Preview() {
    val history = mockWorkingPeriods
    val time = remember { mutableStateOf(10L) }
    val state = remember { mutableStateOf(UIState.Success(Record(OffsetDateTime.now(), MeterState.StateIn))) }
    val filter = remember { mutableStateOf(DateFilter.All) }
    val days = remember { mutableStateOf(DayOfWeek.values().toSet()) }
    val isFilterVisible = remember { mutableStateOf(true) }
    HistoryScreen(PaddingValues(0.dp), state, filter, history, time, days, isFilterVisible, {}, {}, {})
}