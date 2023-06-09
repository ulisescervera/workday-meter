package com.gmail.uli153.workdaymeter.ui.screens.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.gmail.uli153.workdaymeter.ui.viewmodel.DateFilter
import com.gmail.uli153.workdaymeter.ui.views.DayFilterSelector
import com.gmail.uli153.workdaymeter.ui.views.HistoryDropdownMenu
import com.gmail.uli153.workdaymeter.utils.AppDimens
import com.gmail.uli153.workdaymeter.utils.mockWorkingPeriods
import org.threeten.bp.DayOfWeek

@Composable
fun ChartScreen(
    padding: PaddingValues,
    filter: State<DateFilter>,
    history: State<UIState<List<WorkingPeriod>>>,
    selectedDays: State<Set<DayOfWeek>>,
    isFilterVisible: State<Boolean>,
    filterSelectedListener: (DateFilter) -> Unit,
    onDayListChanged: (Set<DayOfWeek>) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize(1f)
        .background(MaterialTheme.colorScheme.background)
        .padding(
            top = padding.calculateTopPadding(),
            bottom = padding.calculateBottomPadding() + 20.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(20.dp)
        ) {
            if (isFilterVisible.value) {
                HistoryDropdownMenu(filter, filterSelectedListener)
                DayFilterSelector(selectedDays, Modifier.fillMaxWidth(), onDayListChanged)
                Spacer(modifier = Modifier.height(AppDimens.rowVSpace))
            }
        }
        WorkingChart(history)
    }
}

@Composable
private fun WorkingChart(history: State<UIState<List<WorkingPeriod>>>) {
    val items: List<WorkingPeriod> = when(val p = history.value) {
        is UIState.Loading -> emptyList()
        is UIState.Success -> p.data.reversed()
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
                setScaleEnabled(false)
                marker = BarChartMarkerView(context) {
                    items.getOrNull(it)?.start
                }
                with(xAxis) {
                    isEnabled = true
                    setDrawGridLines(false)
                    setAvoidFirstLastClipping(true)
                    setDrawAxisLine(true)
                    position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                    valueFormatter = dateValueFormatter
                    setLabelCount(3, false)
                    val description = object: Description() {
                        override fun getText(): String = ""
                    }
                    description.isEnabled = false
                    this@apply.description = description
//                        textColor = getColor(R.color.grey_rollingStone)
//                        axisLineColor = getColor(R.color.grey_background)
                }

                with(axisLeft) {
                    setDrawLabels(false)
                    setDrawAxisLine(false)
                    setDrawGridLines(false)
                    setLabelCount(leftLabelCount, false)
                    valueFormatter = durationValueFormatter
                    textSize = 12.0f
//                        axisLineColor = getColor(R.color.grey_background)
//                        textColor = getColor(R.color.grey_rollingStone)
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
                axisMinimum = 0f
            }

            val dataSet = BarDataSet(entries, "")
            dataSet.color = tertiary
            dataSet.valueFormatter = durationValueFormatter

            val data = BarData(dataSet)
            data.barWidth = 0.40f
            barChart.data = data
            barChart.xAxis.setLabelCount(3, false)
//            barChart.setVisibleXRangeMaximum(10f)
            barChart.setVisibleXRange(1f, 10f)
//            barChart.xAxis.axisMinimum = -data.barWidth / 2f
//            barChart.xAxis.axisMaximum = data.entryCount - data.barWidth / 2f
            barChart.animateY(500)
            barChart.notifyDataSetChanged()
            barChart.invalidate()
        }
    )
}

@Preview
@Composable
fun ChartScreen_Preview() {
    val filter = remember { mutableStateOf(DateFilter.All) }
    val history = remember { mockWorkingPeriods }
    val days = remember { mutableStateOf(DayOfWeek.values().toSet()) }
    val isFilterVisible = remember { mutableStateOf(true) }
    ChartScreen(PaddingValues(0.dp), filter, history, days, isFilterVisible, {}, {})
}