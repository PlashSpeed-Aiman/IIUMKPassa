package org.forthify.passxplat.logic

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import java.io.File

interface FileSave{

    fun SaveToFile(fileName: String, data: ByteArray)

}

class ImaalumService(private val client : HttpClient,val fileSave: FileSave)  {


    // Method to download the course confirmation slip
    suspend fun downloadCourseConfirmationSlip(sessionVal: String, semesterVal: String) {

            val url = "https://imaluum.iium.edu.my/confirmationslip?ses=$sessionVal&sem=$semesterVal"
            val response: HttpResponse = client.get(url)

            if (response.status.value == 200) {
                val bodyBytes = response.readBytes()
                fileSave.SaveToFile("CourseConfirmationSlip.html", bodyBytes)
                println("Course Confirmation Slip download complete")
            } else {
                println("Failed to download Course Confirmation Slip: ${response.status.value}")
            }

    }

    // Method to download the exam slip
    suspend fun downloadExamSlip() {
        CoroutineScope(Dispatchers.IO).launch {
            val url = "https://imaluum.iium.edu.my/examslip" // Example URL, adjust accordingly
            val response: HttpResponse = client.get(url)

            if (response.status.value == 200) {
                val bodyBytes = response.readBytes()
                fileSave.SaveToFile("ExamSlip.html", bodyBytes)
                println("Exam Slip download complete")
            } else {
                println("Failed to download Exam Slip: ${response.status.value}")
            }
        }
    }

    // Method to download the result
    suspend fun downloadResult(sessionVal: String, semesterVal: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val url = "https://imaluum.iium.edu.my/result?ses=$sessionVal&sem=$semesterVal"
            val response: HttpResponse = client.get(url)

            if (response.status.value == 200) {
                val bodyBytes = response.readBytes()
                fileSave.SaveToFile("Result.html", bodyBytes)
                println("Result download complete")
            } else {
                println("Failed to download Result: ${response.status.value}")
            }
        }
    }

}
