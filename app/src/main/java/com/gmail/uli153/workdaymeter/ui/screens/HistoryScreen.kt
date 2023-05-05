package com.gmail.uli153.workdaymeter.ui.screens

import android.content.res.Resources.Theme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.WorkingPeriod
import com.gmail.uli153.workdaymeter.utils.Formatters
import java.text.SimpleDateFormat
import com.gmail.uli153.workdaymeter.R
import com.gmail.uli153.workdaymeter.utils.AppDimens
import com.gmail.uli153.workdaymeter.utils.extensions.formattedTime
import com.gmail.uli153.workdaymeter.utils.mockWorkingPeriods

@Composable
fun HistoryScreen(periods: State<UIState<List<WorkingPeriod>>>) {
    val items: List<WorkingPeriod> = when(val p = periods.value) {
        is UIState.Loading -> emptyList()
        is UIState.Success -> p.data
    }
    val formatter = Formatters.dateTimeHuman
    Box(modifier = Modifier
        .fillMaxSize(1f)
        .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            modifier = Modifier.fillMaxSize(1f)
        ) {
            items(items) {
                WorkingPeriodRow(it, formatter)
                Spacer(modifier = Modifier.height(AppDimens.rowVSpace))
            }
        }
    }
}

@Composable
private fun WorkingPeriodRow(period: WorkingPeriod, formatter: SimpleDateFormat) {
    val end = if (period.end != null) {
        formatter.format(period.end)
    } else {
        stringResource(id = R.string.waiting_clock_out)
    }
    ElevatedCard(shape = RoundedCornerShape(AppDimens.rowCornerRadius),
        modifier = Modifier.fillMaxWidth(1f)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = AppDimens.rowHPadding, vertical = AppDimens.rowVPadding)
        ) {
            Column {
                Text(text = formatter.format(period.start))
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = end)
            }
            if (period.duration != null) {
                Spacer(modifier = Modifier.weight(1f))
                Text(text = period.duration!!.formattedTime)
            }
        }
    }
}

@Preview
@Composable
fun HistoryScreen_Preview() {
    val periods = mockWorkingPeriods
    HistoryScreen(periods)
}