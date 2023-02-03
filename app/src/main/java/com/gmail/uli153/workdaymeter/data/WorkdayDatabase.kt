package com.gmail.uli153.workdaymeter.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gmail.uli153.workdaymeter.data.WorkdayDatabase.Companion.DATABASE_VERSION
import com.gmail.uli153.workdaymeter.data.daos.ClockInOutDao
import com.gmail.uli153.workdaymeter.data.entities.ClockInOutEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(entities = [ClockInOutEntity::class], version = DATABASE_VERSION)
@TypeConverters(Converters::class)
abstract class WorkdayDatabase: RoomDatabase() {

    companion object {
        const val DATABASE_VERSION: Int = 1
        const val DATABASE_NAME: String = "workday_meter_database"

        fun buildDatabase(context: Context): WorkdayDatabase {
            return Room.databaseBuilder(context, WorkdayDatabase::class.java, DATABASE_NAME)
                .addCallback(WorkdayDatabaseCallack())
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun clockInOutDao(): ClockInOutDao

}

class WorkdayDatabaseCallack: RoomDatabase.Callback() {

    private val scope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        val wmDB = db as? WorkdayDatabase ?: return
        val dao = wmDB.clockInOutDao()
        scope.launch(Dispatchers.IO) {
            //todo
        }
    }

}