package com.gmail.uli153.workdaymeter.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gmail.uli153.workdaymeter.data.WorkdayDatabase.Companion.DATABASE_VERSION
import com.gmail.uli153.workdaymeter.data.daos.RecordsDao
import com.gmail.uli153.workdaymeter.data.entities.RecordEntity
import com.gmail.uli153.workdaymeter.domain.toEntity
import com.gmail.uli153.workdaymeter.utils.mockHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@Database(entities = [RecordEntity::class], version = DATABASE_VERSION)
@TypeConverters(Converters::class)
abstract class WorkdayDatabase: RoomDatabase() {

    companion object {
        const val DATABASE_VERSION: Int = 1
        const val DATABASE_NAME: String = "workday_meter_database"

        lateinit var instance: WorkdayDatabase
            private set

        fun buildDatabase(context: Context): WorkdayDatabase {
            return Room.databaseBuilder(context, WorkdayDatabase::class.java, DATABASE_NAME)
                .addCallback(WorkdayDatabaseCallack())
                .fallbackToDestructiveMigration()
                .setQueryCallback(WorkdayQueryCallback(), Executors.newSingleThreadExecutor())
                .allowMainThreadQueries()
                .build().also {
                    instance = it
                }
        }
    }

    abstract fun recordsDao(): RecordsDao

}

class WorkdayDatabaseCallack: RoomDatabase.Callback() {

    private val scope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        val wmDB = WorkdayDatabase.instance
        val dao = wmDB.recordsDao()
        scope.launch(Dispatchers.IO) {
            val mock = mockHistory
            for (r in mock) {
                dao.insert(r.toEntity())
            }
        }
    }

}

class WorkdayQueryCallback: RoomDatabase.QueryCallback {
    override fun onQuery(sqlQuery: String, bindArgs: List<Any?>) {
        Log.d("WorkdayDatabase", sqlQuery +"Args: " + bindArgs.toString())
    }

}