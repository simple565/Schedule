package com.maureen.schedule;

import android.app.Application;

import com.maureen.schedule.data.AppDatabase;

import androidx.room.Room;

/**
 * Function:
 * Create:   2020/11/24
 *
 * @author lianml
 */
public class MyApplication extends Application {

    private AppDatabase mAppDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "TimeTable.db")
                .allowMainThreadQueries()
                .build();
    }

    public AppDatabase getAppDatabase() {
        return mAppDatabase;
    }
}
