package com.gmail.uli153.workdaymeter.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.uli153.workdaymeter.R
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.domain.models.WorkingPeriod
import com.gmail.uli153.workdaymeter.domain.use_cases.RecordUseCases
import com.gmail.uli153.workdaymeter.service.ChronometerService
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
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime
import java.util.Date
import javax.inject.Inject
import kotlin.reflect.KClass

@HiltViewModel
class MainViewModel @Inject constructor(
    private val recordUseCases: RecordUseCases
) : ViewModel() {

    val state: StateFlow<UIState<Record>> = recordUseCases.getStateUseCase()
        .map<Record, UIState<Record>> { UIState.Success(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, UIState.Loading)

    val time: StateFlow<Long> = ChronometerService.time

    private val _filter: MutableStateFlow<HistoryFilter> = MutableStateFlow(HistoryFilter.All)
    val filter: StateFlow<HistoryFilter> = _filter

    private val _history: MutableStateFlow<UIState<List<WorkingPeriod>>> = MutableStateFlow(UIState.Loading)
    val history: StateFlow<UIState<List<WorkingPeriod>>> = _history

    fun toggleState() {
        viewModelScope.launch(Dispatchers.IO) {
            recordUseCases.toggleStateUseCase()
        }
    }

    fun setHistoryFilter(filter: HistoryFilter) {
        _filter.value = filter
    }

    private var currentHistoryJob: Job? = null
    init {
        viewModelScope.launch(Dispatchers.Main) {
            filter.collectLatest { filter ->
                currentHistoryJob?.cancel()
                val flow: Flow<UIState<List<Record>>> = when(filter) {
                    is HistoryFilter.Range -> recordUseCases.getRecordsInRangeUseCase(filter.from, filter.to)
                    HistoryFilter.All -> recordUseCases.getRecordsUseCase()
                    HistoryFilter.Today -> recordUseCases.getTodayRecordsUseCase()
                    HistoryFilter.Week -> recordUseCases.getRecordsWeekUseCase()
                    HistoryFilter.Month -> recordUseCases.getRecordsMonthUseCase()
                    HistoryFilter.Year -> recordUseCases.getRecordsYearUseCase()
                }.map<List<Record>, UIState<List<Record>>> { UIState.Success(it) }.cancellable()

                currentHistoryJob = viewModelScope.launch(Dispatchers.Main) {
                    flow.collectLatest {
                        _history.value = it.toHistory()
                    }
                }
            }
        }
    }
}

enum class HistoryFilterOption {
    Range, All, Today, Week, Month, Year
}

sealed class HistoryFilter {
    data class Range(val from: OffsetDateTime, val to: OffsetDateTime): HistoryFilter()
    object All: HistoryFilter()
    object Today: HistoryFilter()
    object Week: HistoryFilter()
    object Month: HistoryFilter()
    object Year: HistoryFilter()

    fun getNameResId(): Int {
        return when(this) {
            is Range -> R.string.app_name
            All -> R.string.app_name
            Today -> R.string.app_name
            Week -> R.string.app_name
            Month -> R.string.app_name
            Year -> R.string.app_name
        }
    }
}

private fun UIState<List<Record>>.toHistory(): UIState<List<WorkingPeriod>> {
    return when(this) {
        is UIState.Loading -> UIState.Loading
        is UIState.Success -> {
            val periods = mutableListOf<WorkingPeriod>()
            for (i in 0 until this.data.size step 2) {
                val start = this.data.getOrNull(i)?.date ?: break
                val end = this.data.getOrNull(i + 1)?.date ?: break
                periods.add(WorkingPeriod(start, end))
            }
            UIState.Success(periods.reversed())
        }
    }
}