package org.forthify.passxplat.logic

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.pathString

class DesktopPrefsImpl: PrefsPersistance {
    companion object{
        val PATH =  Path(System.getProperty("user.dir"),"prefs.json").pathString
    }
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true;encodeDefaults=true }

    override fun savePreferences(preferences: Preferences) {
        val jsonString = json.encodeToString(preferences)
        println(PATH)
        try{
            File(PATH).writeText(jsonString)

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun loadPreferences(): Preferences {
        return try {
            val jsonString = File(PATH).readText()
            json.decodeFromString(jsonString)
        } catch (e: Exception) {
            Preferences() // Return default preferences if file doesn't exist or is invalid
        }
    }
}