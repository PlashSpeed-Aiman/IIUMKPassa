package org.forthify.passxplat.logic
import arrow.core.Either
import arrow.core.left
import arrow.core.right

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.readBytes
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

sealed class DownloadError : Error() {
    object LoginFailed : DownloadError()
    data class HttpError(val statusCode: Int) : DownloadError()
    data class UnknownError(val exception: Exception) : DownloadError()
}

interface FileSave {

    fun saveToFile(fileName: String, data: ByteArray)
    fun saveToFile(fileName: String, data: ByteArray, onComplete: (Boolean) -> Unit)
}

interface FileView {
    fun ViewFile(filePath: File)
}

class ImaalumService(
    private val client: HttpClient,
    private val fileSave: FileSave,
    private val loginService: LoginService
) {
    private enum class DocumentType(val fileName: String, val fileExtension: String, val url: String) {
        COURSE_CONFIRMATION_SLIP("CourseConfirmationSlip", "html", "https://imaluum.iium.edu.my/confirmationslip"),
        EXAM_SLIP("ExamSlip", "pdf", "https://imaluum.iium.edu.my/MyAcademic/course_timetable"),
        RESULT("Result", "html", "https://imaluum.iium.edu.my/MyAcademic/resultprint"),
        FINANCIAL_STATEMENT("Financial", "pdf", "https://imaluum.iium.edu.my/MyFinancial")
    }

    private suspend fun downloadDocument(
        documentType: DocumentType,
        sessionVal: String? = null,
        semesterVal: String? = null
    ): Either<Boolean, DownloadError> = withContext(Dispatchers.IO) {
        try {
            val imaalumClient = loginService.LoginToImaalum(client)
            val url = when {
                sessionVal != null && semesterVal != null -> "${documentType.url}?ses=$sessionVal&sem=$semesterVal"
                else -> documentType.url
            }

            val response: HttpResponse = imaalumClient.get(url)

            when (response.status.value) {
                200 -> {
                    val inputStream = response.bodyAsChannel().toInputStream()
                    val fileName = "${documentType.fileName}.${documentType.fileExtension}"
                    fileSave.saveToFile(fileName, inputStream.readAllBytes())
                    println("${documentType.name} download complete")
                    true.left()
                }
                else -> {
                    println("Failed to download ${documentType.name}: HTTP ${response.status.value}")
                    DownloadError.HttpError(response.status.value).right()
                }
            }
        } catch (e: Exception) {
            println("Error downloading ${documentType.name}: ${e.message}")
            e.printStackTrace()
            DownloadError.UnknownError(e).right()
        }
    }

    suspend fun downloadCourseConfirmationSlip(sessionVal: String, semesterVal: String): Either<Boolean, Error> =
        downloadDocument(DocumentType.COURSE_CONFIRMATION_SLIP, sessionVal, semesterVal)

    suspend fun downloadExamSlip(): Either<Boolean, Error> =
        downloadDocument(DocumentType.EXAM_SLIP)

    suspend fun downloadResult(sessionVal: String, semesterVal: String): Either<Boolean, Error> =
        downloadDocument(DocumentType.RESULT, sessionVal, semesterVal)

    suspend fun downloadFinancialStatement(): Either<Boolean, Error> =
        downloadDocument(DocumentType.FINANCIAL_STATEMENT)
}

