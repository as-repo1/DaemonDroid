package com.daemondroid.app

import android.app.Application
import com.topjohnwu.superuser.Shell
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DaemonDroidApp : Application() {

    companion object {
        init {
            // Configure libsu: request root silently, set timeout
            Shell.setDefaultBuilder(
                Shell.Builder.create()
                    .setFlags(Shell.FLAG_REDIRECT_STDERR)
                    .setTimeout(30)
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}
