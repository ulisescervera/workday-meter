/**
 * Created by Ulises on 11/5/23.
 */
package com.gmail.uli153.workdaymeter.ui.screens.chart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.gmail.uli153.workdaymeter.R
import com.gmail.uli153.workdaymeter.domain.models.WorkingPeriod
import com.gmail.uli153.workdaymeter.utils.Formatters
import com.gmail.uli153.workdaymeter.utils.extensions.formattedTimeChart

@SuppressLint("ViewConstructor")
class BarChartMarkerView(context: Context, private val items: List<WorkingPeriod>): MarkerView(context, R.layout.chart_marker_view) {

    private val labelDate: TextView = findViewById(R.id.label_date)
    private val labelValue: TextView = findViewById(R.id.label_value)

    private val formatterDate = Formatters.dateTimeHuman
    private val formatterTime = Formatters.timeChart

    override fun refreshContent(e: Entry, highlight: Highlight) {
        labelDate.text = getFormattedDateAtIndex(e.x)
        labelValue.text = e.y.toLong().formattedTimeChart
        super.refreshContent(e, highlight)
    }

    override fun draw(canvas: Canvas?, posX: Float, posY: Float) {
        super.draw(canvas, posX, posY)
        // Nos permite que la vista de no se salga del gráfico
        getOffsetForDrawingAtPoint(posX, posY)
    }

    private fun getFormattedDateAtIndex(index: Float): String {
        val date = items.getOrNull(index.toInt())?.start
        return if (date != null) {
            val now = org.threeten.bp.OffsetDateTime.now()
            if (date.year == now.year && date.dayOfYear == now.dayOfYear) {
                formatterTime.format(date)
            } else {
                formatterDate.format(date)
            }
        } else ""
    }
}