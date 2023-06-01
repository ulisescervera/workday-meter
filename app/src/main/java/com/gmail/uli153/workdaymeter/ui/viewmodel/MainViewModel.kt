package com.gmail.uli153.workdaymeter.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.domain.models.WorkingPeriod
import com.gmail.uli153.workdaymeter.domain.use_cases.RecordUseCases
import com.gmail.uli153.workdaymeter.service.ChronometerService
import com.gmail.uli153.workdaymeter.utils.PreferenceUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.threeten.bp.DayOfWeek
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferenceUtils: PreferenceUtils,
    private val recordUseCases: RecordUseCases
) : ViewModel() {

    val state: StateFlow<UIState<Record>> = recordUseCases.getStateUseCase()
        .map<Record, UIState<Record>> { UIState.Success(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, UIState.Loading)

    val time: StateFlow<Long> = ChronometerService.time

    private val _dateFilter: MutableStateFlow<DateFilter> = MutableStateFlow(DateFilter.All)
    val dateFilter: StateFlow<DateFilter> = _dateFilter

    private val _history: MutableStateFlow<UIState<List<WorkingPeriod>>> = MutableStateFlow(UIState.Loading)
    val history: StateFlow<UIState<List<WorkingPeriod>>> = _history

    private val _dayFilter: MutableStateFlow<Set<DayOfWeek>> = MutableStateFlow(preferenceUtils.getDayFilter())
    val dayFilter: StateFlow<Set<DayOfWeek>> = _dayFilter

    private var currentHistoryJob: Job? = null

    fun toggleState() {
        viewModelScope.launch(Dispatchers.IO) {
            recordUseCases.toggleStateUseCase()
        }
    }

    fun setDateFilter(filter: DateFilter) {
        val oldValue = dateFilter.value
        if (oldValue == filter) return

        preferenceUtils.saveDateFilter(filter)
        _dateFilter.value = filter
        if (filter == DateFilter.Today) {
            _dayFilter.value = setOf(OffsetDateTime.now().dayOfWeek)
        }
        if (oldValue == DateFilter.Today) {
            _dayFilter.value = preferenceUtils.getDayFilter()
        }
    }

    fun setDayFilter(days: Set<DayOfWeek>) {
        if (dateFilter.value == DateFilter.Today) return

        preferenceUtils.saveDayFilter(days)
        _dayFilter.value = days
    }

    init {
        val savedDateFilter = preferenceUtils.getDateFilter()
        if (savedDateFilter != null && savedDateFilter != dateFilter.value) {
            setDateFilter(savedDateFilter)
        }

        viewModelScope.launch(Dispatchers.Main) {
            val filters = combine(dateFilter, dayFilter) { filter, selectedDays ->
                Filters(filter, selectedDays)
            }
            filters.collectLatest {
                val filter = it.dateFilter
                val selectedDays = it.dayFilter
                currentHistoryJob?.cancel()
                val flow: Flow<UIState<List<Record>>> = when(filter) {
                    is DateFilter.Range -> recordUseCases.getRecordsInRangeUseCase(filter.from, filter.to)
                    DateFilter.All -> recordUseCases.getRecordsUseCase()
                    DateFilter.Today -> recordUseCases.getTodayRecordsUseCase()
                    DateFilter.Week -> recordUseCases.getRecordsWeekUseCase()
                    DateFilter.Month -> recordUseCases.getRecordsMonthUseCase()
                    DateFilter.Year -> recordUseCases.getRecordsYearUseCase()
                }.map<List<Record>, UIState<List<Record>>> { UIState.Success(it) }.cancellable()

                currentHistoryJob = viewModelScope.launch(Dispatchers.Main) {
                    flow.collectLatest {
                        _history.value = it.toHistory(selectedDays)
                    }
                }
            }
        }
    }

    private data class Filters(val dateFilter: DateFilter, val dayFilter: Set<DayOfWeek>)
}

private fun UIState<List<Record>>.toHistory(days: Set<DayOfWeek>): UIState<List<WorkingPeriod>> {
    return when(this) {
        is UIState.Loading -> UIState.Loading
        is UIState.Success -> {
            val periods = mutableListOf<WorkingPeriod>()
            for (i in 0 until this.data.size step 2) {
                val start = this.data.getOrNull(i)?.date ?: break
                val end = this.data.getOrNull(i + 1)?.date ?: break
                if (!days.contains(start.dayOfWeek)) continue
                periods.add(WorkingPeriod(start, end))
            }
            UIState.Success(periods.reversed())
        }
    }
}