package org.forthify.passxplat.logic

import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.pathString

///WTF AM I DOING, SHOULD'VE JUST NAME IT PERSISTANCE IN THE FIRST PLACE
class DesktopFileSave(private val desktopPrefsImpl:PrefsPersistance) : FileSave{
    override fun saveToFile(fileName: String, data: ByteArray) {

        val preferences = desktopPrefsImpl.loadPreferences()
        when(preferences.folderLocation){
            "" -> File(fileName).writeBytes(data)
            else->{
                val path = Path(preferences.folderLocation,fileName)
                File(path.pathString).writeBytes(data)
            }
        }

    }

    override fun saveToFile(fileName: String, data: ByteArray, onComplete: (Boolean) -> Unit) {
        return
    }

}