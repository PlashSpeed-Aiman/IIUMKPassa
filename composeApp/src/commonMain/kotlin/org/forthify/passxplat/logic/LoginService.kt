package org.forthify.passxplat.logic

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.forthify.passxplat.model.StudentCredentials

class LoginService(
    private val httpClient: HttpClient,
    private val credentialStorage: CredentialStorage
) {
    companion object {
        private const val WIFI_LOGIN_URL = "https://captiveportalmahallahgombak.iium.edu.my/cgi-bin/login"
        private const val IMAALUM_LOGIN_URL = "https://cas.iium.edu.my:8448/cas/login?service=https%3a%2f%2fimaluum.iium.edu.my%2fhome"
    }

    private fun createWifiLoginForm(credentials: StudentCredentials): Parameters = Parameters.build {
        append("user", credentials.matricNumber)
        append("password", credentials.password)
        append("url", "http://www.iium.edu.my/")
        append("cmd", "authenticate")
        append("Login", "Log In")
    }

    private fun createImaalumLoginForm(credentials: StudentCredentials): Parameters = Parameters.build {
        append("username", credentials.matricNumber)
        append("password", credentials.password)
        append("execution", "e1s1")
        append("_eventId", "submit")
        append("geolocation", "")
    }

    suspend fun loginToWifi(): Either<Boolean, Error> = withContext(Dispatchers.IO) {
        try {
            val credentials = credentialStorage.load()
            val form = createWifiLoginForm(credentials)
            val response = httpClient.post(WIFI_LOGIN_URL) {
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(form.formUrlEncode())
            }
            println("Login to Wifi")
            when {
                response.status.isSuccess() -> true.left()
                else -> Error("Login failed with status: ${response.status}").right()
            }
        } catch (e: Exception) {
            Error(e).right()
        }
    }

    suspend fun loginToImaalum(client: HttpClient): Either<HttpClient, Error> = withContext(Dispatchers.IO) {
        try {
            val credentials = credentialStorage.load()

            // First request to get necessary cookies
            client.get(IMAALUM_LOGIN_URL)

            // Second request to submit the login form
            val loginResponse = client.submitForm(
                url = IMAALUM_LOGIN_URL,
                formParameters = createImaalumLoginForm(credentials)
            )


            println("Imaalum login failed with status: ${loginResponse.status}")
            client.left()

        } catch (e: Exception) {
            Error(e).right()
        }
    }
}