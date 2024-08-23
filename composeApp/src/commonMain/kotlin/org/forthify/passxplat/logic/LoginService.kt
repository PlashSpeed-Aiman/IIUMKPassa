package org.forthify.passxplat.logic

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import org.forthify.passxplat.model.StudentCredentials

class LoginService(private val httpClient: HttpClient,private val credentialStorage: CredentialStorage){

    companion object{
        const val ADDRESS = "https://captiveportalmahallahgombak.iium.edu.my/cgi-bin/login"
    }


    private fun imaalumCreds(studentCredentials: StudentCredentials): Parameters{

// Assuming decodeUser and decodePass are already decoded as strings
        val formData = Parameters.build {
            append("username", studentCredentials.matricNumber)
            append("password", studentCredentials.password)
            append("execution", "e1s1")
            append("_eventId", "submit")
            append("geolocation", "")
        }
        return formData
    }
    private fun createForm(studentCredentials: StudentCredentials) : Parameters{

        val formVal = Parameters.build {
            append("user", studentCredentials.matricNumber)
            append("password", studentCredentials.password)
            append("url", "http://www.iium.edu.my/")
            append("cmd", "authenticate")
            append("Login", "Log In")
        }
        return formVal;
    }
    suspend fun LoginToWifi(){
        val creds = credentialStorage.load()
        val form = createForm(creds)
        try {
            httpClient.post(ADDRESS) {
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(form.formUrlEncode())
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    suspend fun LoginToImaalum(httpClient: HttpClient): HttpClient {
        val ADDRESS = "https://cas.iium.edu.my:8448/cas/login?service=https%3a%2f%2fimaluum.iium.edu.my%2fhome";

        val first_response: HttpResponse = httpClient.request(ADDRESS){
            method = HttpMethod.Get
        }
        val second_response: HttpResponse = httpClient.submitForm(ADDRESS, imaalumCreds(studentCredentials = credentialStorage.load()))
        return httpClient
    }


}
