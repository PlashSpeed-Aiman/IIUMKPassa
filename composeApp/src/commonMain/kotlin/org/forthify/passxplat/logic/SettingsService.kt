package org.forthify.passxplat.logic

import kotlinx.serialization.Serializable


@Serializable
data class Preferences(
    val folderLocation: String = "",
    val isDarkModeEnabled: Boolean = false,
    val notificationCount: Int = 0
)
interface PrefsPersistance {
    fun savePreferences(preferences: Preferences)
    fun loadPreferences(): Preferences
}
class SettingsService(private val prefsPersistance: PrefsPersistance) {

   fun saveToLocation(filePath:String){
       val prefs = prefsPersistance.loadPreferences()
       val x = Preferences(
           folderLocation = filePath,
           prefs.isDarkModeEnabled,
           notificationCount = prefs.notificationCount
       )
       prefsPersistance.savePreferences(x)
   }

    fun loadLocation():String{
        return prefsPersistance
            .loadPreferences()
            .folderLocation
    }
}