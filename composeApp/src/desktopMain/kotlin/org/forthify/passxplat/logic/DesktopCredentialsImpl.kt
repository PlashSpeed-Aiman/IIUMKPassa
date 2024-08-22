package org.forthify.passxplat.logic

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.forthify.passxplat.model.StudentCredentials
import java.io.File


class DesktopCredentialsImpl : CredentialStorage {
    private val credentialsFile = File("credentials.json")
    private val json = Json { prettyPrint = true }

    override fun save(credentials: StudentCredentials) {
        try {
            val jsonString = json.encodeToString(credentials)
            credentialsFile.writeText(jsonString)
        } catch (e: Exception) {
            println("Error saving credentials: ${e.message}")
        }
    }

    override fun load(): StudentCredentials {
        if (!credentialsFile.exists()) {
            return StudentCredentials("", "")
        }

        return try {
            val jsonString = credentialsFile.readText()
            json.decodeFromString<StudentCredentials>(jsonString)
        } catch (e: Exception) {
            println("Error reading credentials: ${e.message}")
            StudentCredentials("", "")
        }
    }

}


