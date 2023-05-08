package com.gmail.uli153.workdaymeter.ui.viewmodel

import com.gmail.uli153.workdaymeter.R
import com.gmail.uli153.workdaymeter.utils.Formatters
import org.threeten.bp.OffsetDateTime

sealed class HistoryFilter(val nameResId: Int, private val id: Int) {
    companion object {
        val values: List<HistoryFilter> get () = listOf(
            All,
            Today,
            Week,
            Month,
            Year,
            Range(OffsetDateTime.now().minusWeeks(1), OffsetDateTime.now())
        )

        fun createFromPreference(str: String): HistoryFilter {
            val splitted = str.split("=")
            val id = splitted[0].toIntOrNull()
            return when(id) {
                0 -> {
                    val from = Formatters.dateTime.parse(splitted[1], OffsetDateTime::from)
                    val to = Formatters.dateTime.parse(splitted[2], OffsetDateTime::from)
                    Range(from, to)
                }
                1 -> All
                2 -> Today
                3 -> Week
                4 -> Month
                5 -> Year
                else -> throw IllegalArgumentException()
            }
        }
    }

    data class Range(val from: OffsetDateTime, val to: OffsetDateTime): HistoryFilter(R.string.filter_range, 0)
    object All: HistoryFilter(R.string.filter_all, 1)
    object Today: HistoryFilter(R.string.filter_today, 2)
    object Week: HistoryFilter(R.string.filter_week, 3)
    object Month: HistoryFilter(R.string.filter_month, 4)
    object Year: HistoryFilter(R.string.filter_year, 5)

    fun toPreference(): String {
        return when(this) {
            is Range -> {
                val from = Formatters.dateTime.format(this.from)
                val to = Formatters.dateTime.format(this.to)
                "$id=$from=$to"
            }
            else -> "$id"
        }
    }
}