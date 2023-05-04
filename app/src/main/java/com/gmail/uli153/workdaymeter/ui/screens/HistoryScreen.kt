package com.gmail.uli153.workdaymeter.ui.screens

import android.content.res.Resources.Theme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.WorkingPeriod
import com.gmail.uli153.workdaymeter.utils.Formatters
import java.text.SimpleDateFormat
import com.gmail.uli153.workdaymeter.R

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
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
            modifier = Modifier.fillMaxSize(1f)
        ) {
            items(items) {
                WorkingPeriodRow(it, formatter)
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

@Composable
private fun WorkingPeriodRow(period: WorkingPeriod, formatter: SimpleDateFormat) {
    Column() {
        val end = if (period.end != null) {
            formatter.format(period.end)
        } else {
            stringResource(id = R.string.waiting_clock_out)
        }
        Text(text = formatter.format(period.start))
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = end)
    }
}