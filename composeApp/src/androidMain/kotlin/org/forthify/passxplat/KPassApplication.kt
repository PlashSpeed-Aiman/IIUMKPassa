package org.forthify.passxplat

import android.app.Application
import org.forthify.passxplat.di.commonModule
import org.forthify.passxplat.logic.AndroidFileSave
import org.forthify.passxplat.logic.AndroidPermissionHandler
import org.forthify.passxplat.logic.FileSave
import org.forthify.passxplat.logic.IPermissionHandler
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class KPassApplication : Application() {
    override fun onCreate() {
        super.onCreate()

    }

}