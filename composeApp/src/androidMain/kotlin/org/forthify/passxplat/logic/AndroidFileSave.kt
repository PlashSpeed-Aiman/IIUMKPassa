package org.forthify.passxplat.logic

import android.Manifest
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.forthify.passxplat.MainActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AndroidFileSave(
    private val context: Context,
) : FileSave {


    override fun saveToFile(fileName: String, data: ByteArray) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveFileApi29AndAbove(fileName, data)
        } else {
            checkAndRequestPermission(
                onGranted = {
                    saveFileBelowApi29(fileName, data)
//                    onComplete(true)
                },
                onDenied = {
//                    onComplete(false)
                }
            )
        }
    }

    override fun saveToFile(fileName: String, data: ByteArray, onComplete: (Boolean) -> Unit) {
        return
    }

    private fun checkAndRequestPermission(onGranted: () -> Unit, onDenied: () -> Unit) {
        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)

        if (ContextCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED) {
            onGranted()
        } else {
            ActivityCompat.requestPermissions( context as MainActivity, permissions, 1001)

        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveFileApi29AndAbove( fileName: String, data: ByteArray) {
        val resolver = context.contentResolver

        // Check if the file already exists
        val existingFileUri = resolver.query(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Downloads._ID),
            "${MediaStore.Downloads.DISPLAY_NAME} = ?",
            arrayOf(fileName),
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID))
                ContentUris.withAppendedId(MediaStore.Downloads.EXTERNAL_CONTENT_URI, id)
            } else {
                null
            }
        }

        // If file exists, delete it
        existingFileUri?.let {
            resolver.delete(it, null, null)
        }

        // Prepare new file content values
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "application/octet-stream")
            put(MediaStore.Downloads.IS_PENDING, 1)
        }

        // Insert the new file
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            try {
                // Write data to the new file
                resolver.openOutputStream(it)?.use { outputStream ->
                    outputStream.write(data)
                }
                // Mark the file as no longer pending
                contentValues.clear()
                contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(it, contentValues, null, null)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun saveFileBelowApi29(fileName: String, data: ByteArray) {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, fileName)

        try {
            FileOutputStream(file).use { fos ->
                fos.write(data)
            }
            Log.println(Log.DEBUG,"DEBUG",downloadsDir.absolutePath + "/" + file.name)
        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        }
    }
}