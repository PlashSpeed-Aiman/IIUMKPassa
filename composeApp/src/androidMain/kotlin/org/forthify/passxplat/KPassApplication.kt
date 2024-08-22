package org.forthify.passxplat

import android.app.Application
import org.forthify.passxplat.di.commonModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KPassApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@KPassApplication)
            modules(commonModule, platformModule())
        }
    }
}