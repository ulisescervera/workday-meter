package com.gmail.uli153.workdaymeter.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmail.uli153.workdaymeter.R
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.MeterState
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.domain.models.WorkingPeriod
import com.gmail.uli153.workdaymeter.ui.viewmodel.HistoryFilter
import com.gmail.uli153.workdaymeter.ui.viewmodel.HistoryFilterOption
import com.gmail.uli153.workdaymeter.utils.AppDimens
import com.gmail.uli153.workdaymeter.utils.Formatters
import com.gmail.uli153.workdaymeter.utils.extensions.formattedTime
import com.gmail.uli153.workdaymeter.utils.mockWorkingPeriods
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun HistoryScreen(
    context: Context,
    state: State<UIState<Record>>,
    filter: State<HistoryFilter>,
    periods: State<UIState<List<WorkingPeriod>>>,
    time: State<Long>,
    setFilter: (HistoryFilter) -> Unit
) {
    val items: List<WorkingPeriod> = when(val p = periods.value) {
        is UIState.Loading -> emptyList()
        is UIState.Success -> p.data
    }
    val formatter = Formatters.dateTimeHuman
    val stateDate = (state.value as? UIState.Success)?.data?.let {
        it.takeIf { it.state == MeterState.StateIn }?.date
    }

    Column(modifier = Modifier
        .fillMaxSize(1f)
        .background(MaterialTheme.colorScheme.background)
    ) {
        HistoryDropdownMenu(context, HistoryFilterOption.values().toList(), filter) {
            val f = when (it) {
                HistoryFilterOption.Range -> HistoryFilter.All
                HistoryFilterOption.All -> HistoryFilter.All
                HistoryFilterOption.Today -> HistoryFilter.Today
                HistoryFilterOption.Week -> HistoryFilter.Week
                HistoryFilterOption.Month -> HistoryFilter.Month
                HistoryFilterOption.Year -> HistoryFilter.Year
            }
            setFilter(f)
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            modifier = Modifier.fillMaxSize(1f)
        ) {
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

@Composable
fun HistoryDropdownMenu(
    context: Context,
    items: List<HistoryFilterOption>,
    selectedItem: State<HistoryFilter>,
    onItemSelected: (HistoryFilterOption) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxWidth(1f)
        .wrapContentSize(Alignment.TopStart)
        .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { expanded.value = true })
            .padding(10.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(CornerSize(10.dp))
            )
        ) {
            Text(text = selectedItem.value.toString(context),)
        }
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .fillMaxWidth(1f)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            for (item in items) {
                DropdownMenuItem(text = { Text(text = stringResource(item.nameResId)) },
                    onClick = {
                        onItemSelected(item)
                        expanded.value = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun HistoryScreen_Preview() {
    val history = mockWorkingPeriods
    val time = remember { mutableStateOf(10L) }
    val state = remember { mutableStateOf(UIState.Success(Record(OffsetDateTime.now(), MeterState.StateIn))) }
    val filter = remember { mutableStateOf(HistoryFilter.All) }
    HistoryScreen(LocalContext.current, state, filter, history, time, {})
}