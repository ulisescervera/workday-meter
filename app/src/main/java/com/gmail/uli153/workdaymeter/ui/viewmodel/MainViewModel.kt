package com.gmail.uli153.workdaymeter.ui.viewmodel

import android.icu.text.Collator.ReorderCodes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.uli153.workdaymeter.domain.Data
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.domain.models.State
import com.gmail.uli153.workdaymeter.domain.use_cases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.coroutineContext

@HiltViewModel
class MainViewModel(
    private val recordUseCase: RecordUseCases
) : ViewModel() {

    val state: StateFlow<Data<Record>> = recordUseCase.getStateUseCase()
        .transformLatest<Record, Data<Record>> { Data.Success(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, Data.Loading)

    val records: StateFlow<Data<List<Record>>> = recordUseCase.getRecordsUseCase()
        .transformLatest<List<Record>, Data<List<Record>>> { Data.Success(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, Data.Loading)

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