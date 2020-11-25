package com.maureen.schedule.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Function:
 * Create:   2020/11/24
 *
 * @author lianml
 */
@Database(entities = {CourseInfoBean.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    /**
     * 获取课程表操作类实例
     * @return
     */
    public abstract CourseInfoDao getCourseInfoDao();


}
