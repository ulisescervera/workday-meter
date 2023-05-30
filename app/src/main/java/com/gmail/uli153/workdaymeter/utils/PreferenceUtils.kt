package com.gmail.uli153.workdaymeter.utils

import android.content.Context
import android.content.SharedPreferences
import com.gmail.uli153.workdaymeter.ui.viewmodel.HistoryFilter
import org.threeten.bp.DayOfWeek

class PreferenceUtils(val context: Context) {

    private enum class PreferenceKeys(val key: String) {
        DATE_FILTER("DATE_FILTER"),
        DAY_FILTER("DAY_FILTER")
    }

    private val PREFERENCE_FILE = "WORDAY_PREFERENCE_FILE"

    private val preferences: SharedPreferences get() {
        return context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE)
    }

    fun saveDateFilter(filter: HistoryFilter) {
        saveString(PreferenceKeys.DATE_FILTER, filter.toPreference())
    }

    fun getDateFilter(): HistoryFilter? {
        val text = getString(PreferenceKeys.DATE_FILTER)?.takeIf { it.isNotBlank() }
        return if (text != null) {
            HistoryFilter.createFromPreference(text)
        } else null
    }

    fun saveDayFilter(filter: Set<DayOfWeek>) {
        val text = filter.joinToString(separator = ";") { it.value.toString() }
        saveString(PreferenceKeys.DAY_FILTER, text)
    }

    fun getDayFilter(): Set<DayOfWeek> {
        val text = getString(PreferenceKeys.DAY_FILTER)?.takeIf { it.isNotBlank() }
        return if (text != null) {
            text.split(";").map { DayOfWeek.of(it.toInt()) }.toSet()
        } else DayOfWeek.values().toSet()
    }

    private fun saveString(key: PreferenceKeys, value: String) {
        preferences.edit().putString(key.key, value).apply()
    }

    private fun getString(key: PreferenceKeys): String? {
        return preferences.getString(key.key, "")
    }
}