package com.yishenghuo.schedule.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Maurren on 2017/11/26.
 */

public class SharedPreferencesUtil {
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    public SharedPreferencesUtil(Context context) {
        this.mContext = context;
        this.mSharedPreferences = context.getSharedPreferences("setting", Context.MODE_APPEND);
    }

    public void putBoolean(String key, boolean value) {
        //throwInit();
        SharedPreferences.Editor edit = this.mSharedPreferences.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }
    public boolean getBoolean(String key, boolean value) {
        //throwInit();
        return mSharedPreferences.getBoolean(key,value);
    }

}
