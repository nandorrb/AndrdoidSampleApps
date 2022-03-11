package com.institutopacifico.actualidad.application

import android.app.PendingIntent.getActivity
import android.content.Context
import android.os.StrictMode

import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import java.security.AccessController.getContext
import android.support.multidex.MultiDex



class ApplicationClass : android.app.Application() {

    private var refWatcher: RefWatcher? = null

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        enabledStrictMode()
        refWatcher = LeakCanary.install(this)
        context = this
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {

      lateinit  var context: Context
            private set

        fun getRefWatcher(context: Context): RefWatcher? {
            val application = context.applicationContext as ApplicationClass
            return application.refWatcher
        }

        private fun enabledStrictMode() {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder() //
                    .detectAll() //
                    .penaltyLog() //
                    .penaltyDeath() //
                    .build())
        }
    }
}