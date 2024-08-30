package org.forthify.passxplat

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.forthify.passxplat.di.commonModule
import org.forthify.passxplat.logic.Preferences
import org.koin.core.context.GlobalContext.startKoin
import java.io.File
import java.io.FileWriter
import kotlin.io.path.pathString

fun ensurePrefExists(){
    var jsonFormatter = Json{encodeDefaults=true}
    val fileName = "prefs.json"
    val file = File(kotlin.io.path.Path(System.getProperty("user.dir"),fileName).pathString)
    when{
        !file.exists() || file.length() == 0L ->{
            val writer = FileWriter(file)
            println(jsonFormatter.encodeToString<Preferences>(Preferences()))
            writer.write(jsonFormatter.encodeToString<Preferences>(Preferences()))
            writer.close()
        }
    }
}

fun main() = application {
    ensurePrefExists()
    Window(
        onCloseRequest = ::exitApplication,
        title = "IIUMKPassa",
        state = rememberWindowState(width = 1000.dp, height = 800.dp),
        ) {
        startKoin {
            modules(commonModule, platformModule())
        }
        App()
    }
}