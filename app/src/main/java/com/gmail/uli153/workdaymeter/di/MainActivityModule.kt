package com.gmail.uli153.workdaymeter.di

import com.gmail.uli153.workdaymeter.domain.use_cases.RecordUseCases
import com.gmail.uli153.workdaymeter.ui.viewmodel.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class MainActivityModule {

    @Provides
    @ViewModelScoped
    fun mainViewModel(recordUseCases: RecordUseCases) = MainViewModel(recordUseCases)

}