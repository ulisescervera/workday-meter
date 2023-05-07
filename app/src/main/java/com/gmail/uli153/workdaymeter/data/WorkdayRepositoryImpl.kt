package com.gmail.uli153.workdaymeter.data

import com.gmail.uli153.workdaymeter.data.entities.ClockState
import com.gmail.uli153.workdaymeter.data.entities.RecordEntity
import com.gmail.uli153.workdaymeter.utils.Formatters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

class WorkdayRepositoryImpl @Inject constructor(
    private val db: WorkdayDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): WorkdayRepository {

    override fun getStateFlow(): Flow<RecordEntity> {
        return channelFlow {
            db.recordsDao().getStateFlow().collectLatest {
                if (it.isEmpty()) {
                    send(RecordEntity(OffsetDateTime.now(), ClockState.ClockOut))
                } else {
                    send(it[0])
                }
            }
        }.flowOn(dispatcher)
    }

    override fun getRecords(): Flow<List<RecordEntity>> {
        return db.recordsDao().getRecords().flowOn(dispatcher)
    }

    override fun getRecordsInRange(from: OffsetDateTime, to: OffsetDateTime): Flow<List<RecordEntity>> {
        val formatter = Formatters.dateTime
        val f = formatter.format(from)
        val t = formatter.format(to)
        return db.recordsDao()
            .getRecordsInRange(f, t)
            .flowOn(dispatcher)
    }

    override suspend fun getState(): RecordEntity {
        return db.recordsDao().getState().let {
            if (it.isEmpty()) {
                RecordEntity(OffsetDateTime.now(), ClockState.ClockOut)
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