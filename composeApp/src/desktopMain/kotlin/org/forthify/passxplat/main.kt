package org.forthify.passxplat

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "IIUMKPassa",
        state = rememberWindowState(width = 400.dp, height = 800.dp),
        ) {
        App()
    }
}