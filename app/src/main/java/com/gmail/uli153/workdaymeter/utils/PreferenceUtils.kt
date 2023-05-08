package com.gmail.uli153.workdaymeter.utils

import android.content.Context
import android.content.SharedPreferences
import com.gmail.uli153.workdaymeter.ui.viewmodel.HistoryFilter

class PreferenceUtils(val context: Context) {

    private enum class PreferenceKeys(val key: String) {
        History("HISTORY")
    }

    private val PREFERENCE_FILE = "WORDAY_PREFERENCE_FILE"

    private val preferences: SharedPreferences get() {
        return context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE)
    }

    fun saveFilter(filter: HistoryFilter) {
        saveString(PreferenceKeys.History, filter.toPreference())
    }

    fun getFilter(): HistoryFilter? {
        val text = getString(PreferenceKeys.History)?.takeIf { it.isNotBlank() }
        return if (text != null) {
            HistoryFilter.createFromPreference(text)
        } else null
    }

    private fun saveString(key: PreferenceKeys, value: String) {
        preferences.edit().putString(key.key, value).apply()
    }

    private fun getString(key: PreferenceKeys): String? {
        return preferences.getString(key.key, "")
    }
}