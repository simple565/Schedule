package com.maureen.schedule;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * Function:
 * Create:   2020/11/24
 *
 * @author lianml
 */
public class MyApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    public static Context getAppContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
