package com.ramaqq.storyapp_submission1.ui

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.ramaqq.storyapp_submission1.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String =
    SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())

// Untuk kasus Intent Camera
fun createTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

// Untuk kasus CameraX
fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (
        mediaDir != null && mediaDir.exists()
    ) mediaDir else application.filesDir

    return File(outputDirectory, "$timeStamp.jpg")
}


// change uri to File
fun uriToFile(selectImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createTempFile(context)

    val inputStream = contentResolver.openInputStream(selectImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    // downloading content data from Uri to myFile...
    while (inputStream.read(buf).also { len = it } > 0)
        outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile

}

// create custom time in calendar
fun getTime(currentDate: Date?, outputDate: SimpleDateFormat): String? {
    val calendar = Calendar.getInstance()
    if (currentDate != null)
        calendar.time = currentDate // date format

    calendar.add(Calendar.HOUR_OF_DAY, 8)
    return outputDate.format(calendar.time)
}
