package com.gmail.uli153.workdaymeter.ui.views

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gmail.uli153.workdaymeter.ui.viewmodel.HistoryFilter
import com.gmail.uli153.workdaymeter.utils.Formatters
import org.threeten.bp.OffsetDateTime

@Composable
fun HistoryDropdownMenu(
    selectedItem: State<HistoryFilter>,
    itemSelectedListener: (HistoryFilter) -> Unit
) {
    val context = LocalContext.current
    val expanded = remember { mutableStateOf(false) }
    val items = remember { mutableStateOf(HistoryFilter.values) }
    val range = selectedItem.value as? HistoryFilter.Range
    val from = range?.from
    val to = range?.to

    Box(modifier = Modifier
        .fillMaxWidth(1f)
        .wrapContentSize(Alignment.Center)
        .padding(vertical = 20.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(CornerSize(10.dp))
            )
            .clickable {
                expanded.value = true
            }
        ) {
            if (from != null) {
                val formatter = Formatters.dateHuman
                val showDateDialog: (OffsetDateTime, Boolean) -> Unit = { date, isFrom ->
                    val listener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                        val newDate = date.withYear(year).withMonth(month + 1).withDayOfMonth(day)
                        val newRange = if (isFrom) range.copy(from = newDate) else range.copy(to = newDate)
                        itemSelectedListener(newRange)
                    }
                    val dialog = DatePickerDialog(context, listener, date.year, date.monthValue - 1, date.dayOfMonth)
                    dialog.show()
                }

                Box(modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(10.dp)) {
                    Text(text = formatter.format(from),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .clickable {
                                showDateDialog(from, true)
                            }
                    )
                    Text(text = "-",
                        modifier = Modifier.align(Alignment.Center)
                    )
                    Text(text = formatter.format(to),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable {
                                showDateDialog(to!!, false)
                            }
                    )
                }
            } else {
                val txt = stringResource(selectedItem.value.nameResId)
                Text(text = txt, modifier = Modifier.padding(10.dp))
            }
        }
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .fillMaxWidth(1f)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            for (item in items.value) {
                DropdownMenuItem(text = { Text(text = stringResource(item.nameResId)) },
                    onClick = {
                        itemSelectedListener(item)
                        expanded.value = false
                    }
                )
            }
        }
    }
}