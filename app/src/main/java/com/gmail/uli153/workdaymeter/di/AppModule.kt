package com.gmail.uli153.workdaymeter.di

import android.content.Context
import com.gmail.uli153.workdaymeter.data.WorkdayDatabase
import com.gmail.uli153.workdaymeter.data.WorkdayRepository
import com.gmail.uli153.workdaymeter.data.WorkdayRepositoryImpl
import com.gmail.uli153.workdaymeter.domain.use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun databaseProvider(@ApplicationContext context: Context): WorkdayDatabase {
        return WorkdayDatabase.buildDatabase(context)
    }

    @Provides
    @Singleton
    fun repositoryProvider(db: WorkdayDatabase): WorkdayRepository {
        return WorkdayRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun recordUsesCases(repository: WorkdayRepository): RecordUseCases {
        return RecordUseCases(
            GetStateUseCase(repository),
            GetRecordsUseCase(repository),
            ToggleStateUseCase(repository),
            DeleteRecordUseCase(repository)
        )
    }
}