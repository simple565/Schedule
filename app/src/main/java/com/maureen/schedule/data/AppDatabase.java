package com.maureen.schedule.data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.maureen.schedule.MyApplication;

/**
 * Function:
 * Create:   2020/11/24
 *
 * @author lianml
 */
@Database(entities = {CourseInfoBean.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "TimeTable.db";
    private static volatile AppDatabase mInstance;

    public static AppDatabase getInstance() {
        if (null == mInstance) {
            synchronized (AppDatabase.class) {
                if (null == mInstance) {
                    mInstance = Room.databaseBuilder(MyApplication.getAppContext(), AppDatabase.class, DATABASE_NAME)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取课程表操作类实例
     *
     * @return
     */
    public abstract CourseInfoDao getCourseInfoDao();
}
