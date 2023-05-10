package com.gmail.uli153.workdaymeter.ui.screens.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.WorkingPeriod
import com.gmail.uli153.workdaymeter.ui.viewmodel.HistoryFilter
import com.gmail.uli153.workdaymeter.ui.views.HistoryDropdownMenu
import com.gmail.uli153.workdaymeter.utils.mockWorkingPeriods

@Composable
fun ChartScreen(
    filter: State<HistoryFilter>,
    history: State<UIState<List<WorkingPeriod>>>,
    filterSelectedListener: (HistoryFilter) -> Unit
) {
    val items: List<WorkingPeriod> = when(val p = history.value) {
        is UIState.Loading -> emptyList()
        is UIState.Success -> p.data
    }

    Column(modifier = Modifier
        .fillMaxSize(1f)
        .background(MaterialTheme.colorScheme.background)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(20.dp)
        ) {
            HistoryDropdownMenu(filter, filterSelectedListener)
        }

        val tertiary = MaterialTheme.colorScheme.tertiary.toArgb()
        val durationValueFormatter = DurationValueFormatter()
        val dateValueFormatter = DateValueFormatter(items)
        val leftLabelCount = 5
        AndroidView(
            modifier = Modifier.fillMaxSize(1f),
            factory = { context  ->
                BarChart(context).apply {
                    legend.isEnabled = false
                    isDragYEnabled = false
                    axisRight.isEnabled = false

                    with(xAxis) {
                        isEnabled = true
//                        textColor = getColor(R.color.grey_rollingStone)
                        setDrawGridLines(false)
//                        axisLineColor = getColor(R.color.grey_background)
                        setDrawAxisLine(true)
                        position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                        valueFormatter = dateValueFormatter
                        setLabelCount(2, false)
                        description = object: Description() {
                            override fun getText(): String = ""
                        }
                    }

                    with(axisLeft) {
//                        axisLineColor = getColor(R.color.grey_background)
                        textSize = 12.0f
//                        textColor = getColor(R.color.grey_rollingStone)
                        setDrawAxisLine(true)
                        setDrawGridLines(false)
                        setLabelCount(leftLabelCount, false)
                        valueFormatter = durationValueFormatter
                    }

                    val renderer = RoundedBarChartRenderer(
                        this,
                        this.animator,
                        this.viewPortHandler
                    )
                    renderer.radius = 8f.dp.value.toInt()
                    this.renderer = renderer
                }
            },
            update = { barChart ->
                val entries = items.mapIndexed { index, workingPeriod ->
                    BarEntry(index.toFloat(), workingPeriod.duration.toFloat())
                }

                barChart.axisLeft.apply {
                    val hourMillis = 1000f * 60 * 60
                    val max = entries.maxOfOrNull { it.y } ?: 1f
                    axisMaximum = 0f
                    if (max <= hourMillis) {
                        axisMaximum = 8 * hourMillis
                    } else {
                        val hours = (max / hourMillis).toInt()
                        axisMaximum = (hours + 1) * hourMillis
                    }
                }

                barChart.xAxis.axisMinimum = entries.getOrNull(0)?.x ?: 0f

                val dataSet = BarDataSet(entries, "")
                dataSet.color = tertiary
                dataSet.valueFormatter = durationValueFormatter

                val data = BarData(dataSet)
                data.barWidth = 0.40f

                barChart.data = data
                barChart.invalidate()
            }
        )
    }
}

@Preview
@Composable
fun ChartScreen_Preview() {
    val filter = remember { mutableStateOf(HistoryFilter.All) }
    val history = remember { mockWorkingPeriods }
    ChartScreen(filter, history, filterSelectedListener = {})
}