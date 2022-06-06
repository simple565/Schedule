package com.maureen.schedule.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maureen.schedule.MainApplication
import com.maureen.schedule.utils.DATABASE_NAME

/**
 * Function:
 * Create:   2020/11/24
 *
 * @author lianml
 */
@Database(
    entities = [Step::class, Task::class,
        Checklist::class, CourseInfo::class], version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun courseInfoDao(): CourseInfoDao
    abstract fun stepDao(): StepDao
    abstract fun taskDao(): TaskDao
    abstract fun checklistDao(): ChecklistDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(MainApplication.appContext!!).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}