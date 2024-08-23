package org.forthify.passxplat.logic

import org.forthify.passxplat.model.StudentCredentials

import android.content.Context
import android.content.SharedPreferences

class AndroidCredentialsImpl(context: Context) : CredentialStorage {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    override fun load(): StudentCredentials {
        val username = sharedPreferences.getString(KEY_USERNAME, "") ?: ""
        val password = sharedPreferences.getString(KEY_PASSWORD, "") ?: ""
        return StudentCredentials(username, password)
    }

    override fun save(credentials: StudentCredentials) {
        sharedPreferences.edit().apply {
            putString(KEY_USERNAME, credentials.matricNumber)
            putString(KEY_PASSWORD, credentials.password)
            apply()
        }
    }

    companion object {
        private const val PREF_NAME = "StudentCredentials"
        private const val KEY_USERNAME = "matricNumber"
        private const val KEY_PASSWORD = "password"
    }
}