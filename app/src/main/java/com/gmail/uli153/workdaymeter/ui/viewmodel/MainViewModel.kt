package com.gmail.uli153.workdaymeter.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.domain.models.WorkingPeriod
import com.gmail.uli153.workdaymeter.domain.use_cases.RecordUseCases
import com.gmail.uli153.workdaymeter.service.ChronometerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val recordUseCase: RecordUseCases
) : ViewModel() {

    val state: StateFlow<UIState<Record>> = recordUseCase.getStateUseCase()
        .map<Record, UIState<Record>> { UIState.Success(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, UIState.Loading)

    val time: StateFlow<Long> = ChronometerService.time
        .asFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, ChronometerService.time.value!!)

    val records: StateFlow<UIState<List<Record>>> = recordUseCase.getRecordsUseCase()
        .map<List<Record>, UIState<List<Record>>> { UIState.Success(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, UIState.Loading)

    val workingPeriods: StateFlow<UIState<List<WorkingPeriod>>> = records.map {
        when(it) {
            is UIState.Loading -> UIState.Loading
            is UIState.Success -> {
                val periods = mutableListOf<WorkingPeriod>()
                for (i in 0 until it.data.size step 2) {
                    val start = it.data.getOrNull(i)?.date ?: break
                    val end = it.data.getOrNull(i + 1)?.date
                    periods.add(WorkingPeriod(start, end))
                }
                UIState.Success(periods.reversed())
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, UIState.Loading)

//    val todayRecords: StateFlow<List<Record>> = flow<List<Record>> {
//        records.collectLatest { data ->
//            when(data) {
//                is Data.Loading -> emit(emptyList())
//                is Data.Success -> {
//                    val today = data.data.filter {  }
//                }
//            }
//        }
//    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun toggleState() {
        viewModelScope.launch(Dispatchers.IO) {
            recordUseCase.toggleStateUseCase()
        }
    }
}