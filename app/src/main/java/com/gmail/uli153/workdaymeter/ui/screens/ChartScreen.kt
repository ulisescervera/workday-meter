package com.gmail.uli153.workdaymeter.ui.screens

import android.graphics.Canvas
import android.graphics.RectF
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.HorizontalBarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.WorkingPeriod
import com.gmail.uli153.workdaymeter.ui.viewmodel.HistoryFilter
import com.gmail.uli153.workdaymeter.ui.views.HistoryDropdownMenu
import com.gmail.uli153.workdaymeter.utils.Formatters
import com.gmail.uli153.workdaymeter.utils.extensions.formattedTime
import com.gmail.uli153.workdaymeter.utils.extensions.millisSince
import org.threeten.bp.OffsetDateTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                        setDrawAxisLine(false)
                        position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                        valueFormatter = dateValueFormatter
                        setLabelCount(2, false)
                    }

                    with(axisLeft) {
//                        axisLineColor = getColor(R.color.grey_background)
                        textSize = 12.0f
//                        textColor = getColor(R.color.grey_rollingStone)
                        setDrawAxisLine(false)
                        setDrawGridLines(false)
                        setLabelCount(leftLabelCount, false)
                        valueFormatter = durationValueFormatter
                    }

//                    val renderer = RoundedHorizontalBarChartRenderer(this, this.animator, this.viewPortHandler)
//                    renderer.setRightRadius(5f)
//                    this.renderer = renderer
                }
            },
            update = { barChart ->
                val entries = items.mapIndexed { index, workingPeriod ->
                    BarEntry(index.toFloat(), workingPeriod.duration.toFloat())
                }
                barChart.axisLeft.apply {
                    val step = 1000f * 60 * 60
                    val max = entries.maxOfOrNull { it.y } ?: 1f
                    axisMaximum = 0f
                    if (max <= step) {
                        axisMaximum = 8 * step
                    } else {
                        val hours = (max / step).toInt()
                        axisMaximum = (hours + 1) * step
                    }
                }
                barChart.xAxis.axisMinimum = entries.getOrNull(0)?.x ?: OffsetDateTime.now().toInstant().toEpochMilli().toFloat()
                val dataSet = BarDataSet(entries, "").apply {
                    color = 0xFFFF0000.toInt()
                    valueFormatter = durationValueFormatter
                }
                val data = BarData(dataSet).apply {
                    barWidth = 0.40f


                }
                barChart.data = data
                barChart.invalidate()
            }
        )
    }
}

private class DateValueFormatter(val items: List<WorkingPeriod>): ValueFormatter() {

    private val formatter = Formatters.dateTimeHuman

    override fun getFormattedValue(value: Float): String {
        val date = items.getOrNull(value.toInt())?.start
        return if (date != null) {
            formatter.format(date)
        } else ""
    }
}

private class DurationValueFormatter: ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return value.toLong().formattedTime
    }
}

class RoundedHorizontalBarChartRenderer(chart: BarDataProvider?, animator: ChartAnimator?, viewPortHandler: ViewPortHandler?) : HorizontalBarChartRenderer(chart, animator, viewPortHandler) {
    private var mRightRadius = 5f
    private var mLeftRadius = 5f

    fun setRightRadius(mRightRadius: Float) {
        this.mRightRadius = mRightRadius
    }

    fun setLeftRadius(mLeftRadius: Float) {
        this.mLeftRadius = mLeftRadius
    }

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val trans = mChart.getTransformer(dataSet.axisDependency)
        mShadowPaint.color = dataSet.barShadowColor
        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY

        // initialize the buffer
        val buffer = mBarBuffers[index]
        buffer.setPhases(phaseX, phaseY)
        buffer.setDataSet(index)
        buffer.setBarWidth(mChart.barData.barWidth)
        buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
        buffer.feed(dataSet)
        trans.pointValuesToPixel(buffer.buffer)

        // if multiple colors
        if (dataSet.colors.size > 1) {
            var j = 0
            while (j < buffer.size()) {
                if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) {
                    j += 4
                    continue
                }
                if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j])) break
                if (mChart.isDrawBarShadowEnabled) {
                    if (mRightRadius > 0) {
                        c.drawRoundRect(
                            RectF(
                                buffer.buffer[j], mViewPortHandler.contentTop(),
                                buffer.buffer[j + 2],
                                mViewPortHandler.contentBottom()
                            ), mRightRadius, mRightRadius, mShadowPaint
                        )
                    } else {
                        c.drawRect(
                            buffer.buffer[j], mViewPortHandler.contentTop(),
                            buffer.buffer[j + 2],
                            mViewPortHandler.contentBottom(), mShadowPaint
                        )
                    }
                }

                // Set the color for the currently drawn value. If the index
                // is
                // out of bounds, reuse colors.
                mRenderPaint.color = dataSet.getColor(j / 4)
                if (mRightRadius > 0) {
                    c.drawRoundRect(
                        RectF(
                            buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                            buffer.buffer[j + 3]
                        ), mRightRadius, mRightRadius, mRenderPaint
                    )
                } else {
                    c.drawRect(
                        buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                        buffer.buffer[j + 3], mRenderPaint
                    )
                }
                j += 4
            }
        } else {
            mRenderPaint.color = dataSet.color
            var j = 0
            while (j < buffer.size()) {
                if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) {
                    j += 4
                    continue
                }
                if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j])) break
                if (mChart.isDrawBarShadowEnabled) {
                    if (mRightRadius > 0) c.drawRoundRect(
                        RectF(
                            buffer.buffer[j], mViewPortHandler.contentTop(),
                            buffer.buffer[j + 2],
                            mViewPortHandler.contentBottom()
                        ), mRightRadius, mRightRadius, mShadowPaint
                    ) else c.drawRect(
                        buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                        buffer.buffer[j + 3], mRenderPaint
                    )
                }
                if (mRightRadius > 0) {
                    c.drawRoundRect(
                        RectF(
                            buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                            buffer.buffer[j + 3]
                        ), mRightRadius, mRightRadius, mRenderPaint
                    )
                } else {
                    c.drawRect(
                        buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                        buffer.buffer[j + 3], mRenderPaint
                    )
                }
                j += 4
            }
        }
    }
}
