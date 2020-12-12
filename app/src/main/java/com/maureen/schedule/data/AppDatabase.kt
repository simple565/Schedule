package com.maureen.schedule.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maureen.schedule.MyApplication
import com.maureen.schedule.utils.DATABASE_NAME

/**
 * Function:
 * Create:   2020/11/24
 *
 * @author lianml
 */
@Database(entities = [CourseInfoBean::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    /**
     * 获取课程表操作类实例
     *
     * @return
     */
    abstract fun courseInfoDao(): CourseInfoDao


    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(MyApplication.appContext!!).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build()
        }
    }
}