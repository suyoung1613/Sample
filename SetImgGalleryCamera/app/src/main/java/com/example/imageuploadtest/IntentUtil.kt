package com.example.imageuploadtest

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


const val REQ_SELECT_IMG = 200
const val REQ_IMG_CAPTURE = 300
const val REQ_IMG_CAPTURE_FULL_SIZE = 400


val pickIntent = Intent(Intent.ACTION_PICK).apply {
    type = MediaStore.Images.Media.CONTENT_TYPE
    data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
}
val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

object IntentMaker {
    lateinit var currentPhotoPath: String
    public fun getFullSizePictureIntent(context: Context): Intent {
        currentPhotoPath = ""//초기화

        val fullSizeCaptureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Ensure that there's a camera activity to handle the intent
        //1) File 생성 - 촬영 사진이 저장 될
        val photoFile: File? = try {
            createImageFile(context)
        } catch (ex: IOException) {
            // Error occurred while creating the File //todo
            null
        }
        // Continue only if the File was successfully created
        photoFile?.also {
            //2) 생성된 File로 부터 Uri 생성 (by FileProvider)
            val photoURI: Uri = FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".fileprovider",
                it
            )
            //3) 생성된 Uri를 Intent에 Put
            fullSizeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        }

        return fullSizeCaptureIntent
    }

    @Throws(IOException::class)
    private fun createImageFile(context: Context): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
}