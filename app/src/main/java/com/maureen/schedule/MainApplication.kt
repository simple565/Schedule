package com.maureen.schedule

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * Function:
 * Create:   2020/11/24
 *
 * @author lianml
 */
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var appContext: Context? = null
    }
}