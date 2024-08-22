package org.forthify.passxplat

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.forthify.passxplat.di.commonModule
import org.koin.core.context.GlobalContext.startKoin

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "IIUMKPassa",
        state = rememberWindowState(width = 400.dp, height = 800.dp),
        ) {
        startKoin {
            modules(commonModule, platformModule())
        }
        App()
    }
}