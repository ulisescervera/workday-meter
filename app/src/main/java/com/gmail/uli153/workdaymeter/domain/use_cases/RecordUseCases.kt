package com.gmail.uli153.workdaymeter.domain.use_cases

data class RecordUseCases(
    val getStateUseCase: GetStateUseCase,
    val getRecordsUseCase: GetRecordsUseCase,
    val toggleStateUseCase: ToggleStateUseCase,
    val deleteRecordUseCase: DeleteRecordUseCase
)