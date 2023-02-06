package com.gmail.uli153.workdaymeter.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.uli153.workdaymeter.domain.models.Record
import com.gmail.uli153.workdaymeter.domain.models.State
import com.gmail.uli153.workdaymeter.domain.use_cases.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.*

class MainViewModel(
    private val recordUseCase: RecordUseCases
) : ViewModel() {

    val state: StateFlow<Record> = recordUseCase.getStateUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = Record(Date(), State.StateOut)
    )

    val records: StateFlow<List<Record>> = recordUseCase.getRecordsUseCase().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )
}