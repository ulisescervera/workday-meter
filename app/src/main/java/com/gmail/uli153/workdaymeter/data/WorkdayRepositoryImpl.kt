package com.gmail.uli153.workdaymeter.data

import com.gmail.uli153.workdaymeter.data.entities.ClockState
import com.gmail.uli153.workdaymeter.data.entities.RecordEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*

class WorkdayRepositoryImpl(
    private val db: WorkdayDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): WorkdayRepository {

    override fun getStateFlow(): Flow<RecordEntity> {
        return flow {
            db.recordsDao().getStateFlow().collectLatest {
                if (it.isEmpty()) {
                    emit(RecordEntity(Date(), ClockState.ClockOut))
                } else {
                    emit(it[0])
                }
            }
        }.flowOn(dispatcher)
    }

    override fun getRecords(): Flow<List<RecordEntity>> {
        return db.recordsDao().getRecords().flowOn(dispatcher)
    }

    override fun getTodayRecords(): Flow<List<RecordEntity>> {
        return db.recordsDao().getTodayRecords().flowOn(dispatcher)
    }

    override suspend fun getState(): RecordEntity {
        return db.recordsDao().getState().let {
            if (it.isEmpty()) {
                RecordEntity(Date(), ClockState.ClockOut)
            } else {
                it[0]
            }
        }
    }

    override suspend fun insert(record: RecordEntity) {
        db.recordsDao().insert(record)
    }

    override suspend fun delete(record: RecordEntity) {
        db.recordsDao().delete(record)
    }
}