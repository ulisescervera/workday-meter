package com.gmail.uli153.workdaymeter.domain.use_cases

data class RecordUseCases(
    val getStateUseCase: GetStateUseCase,
    val toggleStateUseCase: ToggleStateUseCase,
    val deleteRecordUseCase: DeleteRecordUseCase,
    val getRecordsUseCase: GetRecordsUseCase,
    val getRecordsInRangeUseCase: GetRecordsInRangeUseCase,
    val getTodayRecordsUseCase: GetTodayRecordsUseCase,
    val getRecordsWeekUseCase: GetRecordsWeekUseCase,
    val getRecordsMonthUseCase: GetRecordsMonthUseCase,
    val getRecordsYearUseCase: GetRecordsYearUseCase
)