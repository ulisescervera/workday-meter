/**
 * Created by Ulises on 27/4/23.
 */
package com.gmail.uli153.workdaymeter.di

import com.gmail.uli153.workdaymeter.data.WorkdayRepository
import com.gmail.uli153.workdaymeter.domain.use_cases.GetStateUseCase
import com.gmail.uli153.workdaymeter.domain.use_cases.ToggleStateUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
class ServiceModule {
    @ServiceScoped
    @Provides
    fun provideToggleStateUseCase(repository: WorkdayRepository) = ToggleStateUseCase(repository)

    @ServiceScoped
    @Provides
    fun provideGetStateUseCase(repository: WorkdayRepository) = GetStateUseCase(repository)
}