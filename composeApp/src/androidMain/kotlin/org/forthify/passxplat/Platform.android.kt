package org.forthify.passxplat

import android.os.Build
import org.forthify.passxplat.logic.AndroidCredentialsImpl
import org.forthify.passxplat.logic.AndroidFileSave
import org.forthify.passxplat.logic.AndroidPermissionHandler
import org.forthify.passxplat.logic.CredentialStorage
import org.forthify.passxplat.logic.FileSave
import org.forthify.passxplat.logic.IPermissionHandler
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.scope.get
import org.koin.dsl.module

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun platformModule(): Module {
    return module {

        single<CredentialStorage>{
            AndroidCredentialsImpl(get())
        }
    }
}