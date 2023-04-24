package com.gmail.uli153.workdaymeter.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.uli153.workdaymeter.domain.UIState
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.domain.use_cases.RecordUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val recordUseCase: RecordUseCases
) : ViewModel() {

    val state: StateFlow<UIState<Record>> = recordUseCase.getStateUseCase()
        .transformLatest<Record, UIState<Record>> { UIState.Success(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, UIState.Loading)

    val records: StateFlow<UIState<List<Record>>> = recordUseCase.getRecordsUseCase()
        .transformLatest<List<Record>, UIState<List<Record>>> { UIState.Success(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, UIState.Loading)

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