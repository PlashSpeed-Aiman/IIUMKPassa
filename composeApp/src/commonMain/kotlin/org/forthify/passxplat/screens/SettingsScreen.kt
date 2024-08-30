package org.forthify.passxplat.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.forthify.passxplat.logic.SettingsService
import javax.swing.JFileChooser


val primaryColor = Color(0xFF00928F)

@Composable
fun SettingsScreen(settingsService: SettingsService) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }
    var autoSaveEnabled by remember { mutableStateOf(true) }
    var selectedFolder by remember { mutableStateOf("") }

    LaunchedEffect(selectedFolder){
        selectedFolder = settingsService.loadLocation()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SettingItem(
            title = "Enable Notifications",
            checked = notificationsEnabled,
            onCheckedChange = { notificationsEnabled = it }
        )

        SettingItem(
            title = "Dark Mode",
            checked = darkModeEnabled,
            onCheckedChange = { darkModeEnabled = it }
        )


        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Folder Directory",style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(8.dp))
        when(isJvm()){
            true -> FolderSelector(selectedFolder = selectedFolder,
                onFolderSelected = { selectedFolder = it
                                   settingsService.saveToLocation(selectedFolder)
                                   },
                true
            )
            else -> Text(
                text = "Folder selection is not available on Android",
                fontStyle = FontStyle.Italic
            )
        }


        Spacer(modifier = Modifier.height(24.dp))
        DisclaimerSection()
    }
}

@Composable
fun SettingItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.1f)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, style = MaterialTheme.typography.body1)
        Text(text = "COMING SOON", fontStyle = FontStyle.Italic, style = MaterialTheme.typography.body2)

//        Switch(
//            checked = checked,
//            onCheckedChange = onCheckedChange,
//            colors = SwitchDefaults.colors(
//                checkedThumbColor = primaryColor
//            )
//        )
    }
}



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FolderSelector(
    selectedFolder: String,
    onFolderSelected: (String) -> Unit,
    enabled:Boolean
) {
    var textFieldSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }
    val customButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = primaryColor,
        contentColor = Color.White
    )
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            enabled = false,
            readOnly = true,
            value = selectedFolder,
            placeholder = { Text("Select a folder to save your downloaded files") },
            onValueChange = { /* Read-only */ },
            modifier = Modifier.fillMaxWidth() .onGloballyPositioned { coordinates ->
                // Capture the size of the TextField
                textFieldSize = coordinates.size.toSize()
            }.pointerInput(
                Unit
            ){
                coroutineScope {
                    detectTapGestures (onTap = {
                        offset ->
                        if (isInsideTextField(offset, textFieldSize))
                        launch {
                            val folder = selectFolder()
                            if (folder != null) {
                                onFolderSelected(folder)
                            }
                        }

                    })
                }
            },

        )

        Spacer(modifier = Modifier.height(8.dp))

        Row (modifier = Modifier.align(Alignment.End)){

            Button(
                colors = customButtonColors,
                enabled = enabled,
                onClick = {
                    onFolderSelected("")
                },
            ) {
                Text("Reset")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                colors = customButtonColors,
                enabled = enabled,
                onClick = {
                    val folder = selectFolder()
                    if (folder != null) {
                        onFolderSelected(folder)
                    }
                },
            ) {
                Text("Save")
            }
        }
    }
}

@Composable
fun DisclaimerSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Disclaimer",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Your privacy and security are important to us. We want to assure you that any credentials or sensitive information you enter in this app are stored locally on your device only. We do not collect, transmit, or store this information on any external servers.",
                style = MaterialTheme.typography.body2
            )
        }
    }
}

private fun selectFolder(): String? {
    return when {
        isJvm() -> selectFolderJvm()
        isAndroid() -> selectFolderAndroid()
        else -> null
    }
}

private fun isJvm(): Boolean {
    return try {
        Class.forName("java.awt.FileDialog")
        true
    } catch (e: ClassNotFoundException) {
        false
    }
}

private fun isAndroid(): Boolean {
    return try {
        Class.forName("android.os.Build")
        true
    } catch (e: ClassNotFoundException) {
        false
    }
}

private fun selectFolderJvm(): String? {
    return try {
        val fileChooser = JFileChooser().apply {
            fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            dialogTitle = "Select a folder"
        }
        val result = fileChooser.showOpenDialog(null)
        if (result == JFileChooser.APPROVE_OPTION) {
            fileChooser.selectedFile.absolutePath
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun selectFolderAndroid(): String? {
    // Placeholder for Android implementation
    println("Android folder selection is not implemented in this example")
    return null
}

fun isInsideTextField(offset: Offset, textFieldSize: androidx.compose.ui.geometry.Size): Boolean {
    return offset.x >= 0f && offset.x <= textFieldSize.width &&
            offset.y >= 0f && offset.y <= textFieldSize.height
}