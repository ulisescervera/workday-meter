/**
 * Created by Ulises on 10/5/23.
 */
package com.gmail.uli153.workdaymeter.ui.screens.chart

import com.github.mikephil.charting.formatter.ValueFormatter
import com.gmail.uli153.workdaymeter.domain.models.WorkingPeriod
import com.gmail.uli153.workdaymeter.utils.Formatters
import org.threeten.bp.OffsetDateTime

class DateValueFormatter(private val items: List<WorkingPeriod>): ValueFormatter() {

    private val formatterDate = Formatters.dateChart
    private val formatterTime = Formatters.timeChart

    override fun getFormattedValue(value: Float): String {
        val date = items.getOrNull(value.toInt())?.start
        return if (date != null) {
            val now = OffsetDateTime.now()
            if (date.year == now.year && date.dayOfYear == now.dayOfYear) {
                formatterTime.format(date)
            } else {
                formatterDate.format(date)
            }
        } else ""
    }
}