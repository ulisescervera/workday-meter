/**
 * Created by Ulises on 10/5/23.
 */
package com.gmail.uli153.workdaymeter.ui.screens.chart

import com.github.mikephil.charting.formatter.ValueFormatter
import com.gmail.uli153.workdaymeter.utils.extensions.formattedTimeChart

class DurationValueFormatter: ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return value.toLong().formattedTimeChart
    }
}