package org.forthify.passxplat.logic

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readBytes
import java.io.File

interface FileSave {

    fun SaveToFile(fileName: String, data: ByteArray)
    fun SaveToFile(fileName: String, data: ByteArray, onComplete: (Boolean) -> Unit)
}

interface FileView {
    fun ViewFile(filePath: File)
}

class ImaalumService(
    private val client: HttpClient,
    val fileSave: FileSave,
    private val loginService: LoginService
) {

    // Method to download the course confirmation slip
    suspend fun downloadCourseConfirmationSlip(sessionVal: String, semesterVal: String) {

        try {
            var imaalumClient = loginService.LoginToImaalum(client)
            val url =
                "https://imaluum.iium.edu.my/confirmationslip?ses=$sessionVal&sem=$semesterVal"
            val response: HttpResponse = imaalumClient.get(url)

            if (response.status.value == 200) {
                val bodyBytes = response.readBytes()
                fileSave.SaveToFile("CourseConfirmationSlip.html", bodyBytes)
                println("Course Confirmation Slip download complete")
            } else {
                println("Failed to download Course Confirmation Slip: ${response.status.value}")
            }
        } catch (exp: Exception) {
            exp.printStackTrace()
        }

    }

    // Method to download the exam slip
    suspend fun downloadExamSlip() {
        try {

            var imaalumClient = loginService.LoginToImaalum(client)

            val url =
                "https://imaluum.iium.edu.my/MyAcademic/course_timetable" // Example URL, adjust accordingly
            val response: HttpResponse = imaalumClient.get(url)

            if (response.status.value == 200) {
                val bodyBytes = response.readBytes()
                fileSave.SaveToFile("ExamSlip.pdf", bodyBytes)
                println("Exam Slip download complete")
            } else {
                println("Failed to download Exam Slip: ${response.status.value}")
            }

        } catch (exp: Exception) {
            exp.printStackTrace()
        }
    }

    // Method to download the result
    suspend fun downloadResult(sessionVal: String, semesterVal: String) {
        try {
            var imaalumClient = loginService.LoginToImaalum(client)

            val url =
                "https://imaluum.iium.edu.my/MyAcademic/resultprint?ses=$sessionVal&sem=$semesterVal"
            val response: HttpResponse = imaalumClient.get(url)

            if (response.status.value == 200) {
                val bodyBytes = response.readBytes()
                fileSave.SaveToFile("Result.html", bodyBytes)
                println("Result download complete")
            } else {
                println("Failed to download Result: ${response.status.value}")
            }
        } catch (exp: Exception) {
            exp.printStackTrace()
        }
    }


    suspend fun downloadFinancialStatement(): Boolean {
        try {
            var imaalumClient = loginService.LoginToImaalum(client)
            val url = "https://imaluum.iium.edu.my/MyFinancial"
            val response: HttpResponse = imaalumClient.get(url)

            if (response.status.value == 200) {
                val bodyBytes = response.readBytes()
                fileSave.SaveToFile("Financial.pdf", bodyBytes)
                println("Result download complete")
                return true
            } else {
                println("Failed to download Result: ${response.status.value}")
            }
        } catch (exp: Exception) {
            exp.printStackTrace()
        }
        return false
    }
}

