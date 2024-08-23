@file:OptIn(ExperimentalPermissionsApi::class)

package org.forthify.passxplat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import org.forthify.passxplat.di.commonModule
import org.forthify.passxplat.logic.AndroidFileSave
import org.forthify.passxplat.logic.AndroidPermissionHandler
import org.forthify.passxplat.logic.FileSave
import org.forthify.passxplat.logic.IPermissionHandler
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainActivity : ComponentActivity() {


    private lateinit var requestMultiplePermissionsLauncher:
            ActivityResultLauncher<Array<String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin{
            androidContext(this@MainActivity)

            modules(commonModule, platformModule(),androidModule)
        }
        requestMultiplePermissionsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach {
                    //Handles permission result
                }
            }
        setContent {
            App()
        }
    }
    private val androidModule = module {


        single<IPermissionHandler> {
            AndroidPermissionHandler( inject())
        }
        single<FileSave>{
            AndroidFileSave(get())
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {

    App()
}