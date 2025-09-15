package com.example.qrify

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveQR(private val context: Context) {
    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun saveImage(bitmap: Bitmap) {
        var success = false
        withContext(Dispatchers.IO) {
            val resolver = context.contentResolver

            val imageCollection = MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY
            )
            val timeInMillis = System.currentTimeMillis()

            val imageContentvalues = ContentValues().apply {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.Images.Media.DISPLAY_NAME, "${timeInMillis}_imageJPG")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.DATE_TAKEN, timeInMillis)
                put(MediaStore.MediaColumns.IS_PENDING, 1)

            }

            val imageMediaStoreUri = resolver.insert(imageCollection, imageContentvalues)


            imageMediaStoreUri?.let { uri ->
                try {
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        bitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            100,
                            outputStream
                        )

                        imageContentvalues.clear()
                        imageContentvalues.put(
                            MediaStore.MediaColumns.IS_PENDING, 0
                        )

                        resolver.update(uri, imageContentvalues, null, null)
                        success = true

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    resolver.delete(uri, null, null)
                }
            }


        }
        withContext(Dispatchers.Main) {
            if (success) {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, "Failed to Save", Toast.LENGTH_SHORT).show()

            }
        }
    }

}