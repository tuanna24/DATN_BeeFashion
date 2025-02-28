package fpl.md19.beefashion.requests

import android.content.Context
import android.net.Uri
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import android.content.ContentResolver
import android.provider.OpenableColumns
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

fun createMultipartBody(uri: Uri, context: Context): MultipartBody.Part? {
    // Tạo tên file từ URI
    val fileName = getFileName(uri, context)

    // Đọc file từ URI
    val file = File(context.cacheDir, fileName)
    try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }

    // Tạo RequestBody từ file
    val requestBody = RequestBody.create(
        context.contentResolver.getType(uri)?.let { it.toMediaTypeOrNull() },
        file
    )

    // Tạo MultipartBody.Part
    return MultipartBody.Part.createFormData("file", file.name, requestBody)
}

private fun getFileName(uri: Uri, context: Context): String {
    var name = "avatar_${System.currentTimeMillis()}.jpg"
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            name = it.getString(displayNameIndex) ?: name
        }
    }
    return name
}
