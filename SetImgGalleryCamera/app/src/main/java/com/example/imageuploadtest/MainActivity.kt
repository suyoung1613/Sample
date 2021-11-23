package com.example.imageuploadtest

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.WebChromeClient
import com.example.imageuploadtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQ_SELECT_IMG = 100
        const val REQ_CAPTURE_IMG = 200
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvGallery.setOnClickListener { pickImg() }
        binding.tvCamera.setOnClickListener { picture() }
    }


    private fun pickImg() {
        val pickIntent = Intent(Intent.ACTION_PICK).apply {
            type = MediaStore.Images.Media.CONTENT_TYPE
            data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
//        val intent = Intent(Intent.ACTION_GET_CONTENT){
//            intent.setType("image/*");
//        }

        val pickTitle = "Select~"
        val chooserIntent = Intent.createChooser(pickIntent, pickTitle)
        startActivityForResult(chooserIntent, REQ_SELECT_IMG)
    }

    private fun picture() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        pictureIntent.resolveActivity(packageManager)?.let {
            startActivityForResult(pictureIntent, REQ_CAPTURE_IMG)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        Log.w("syTest", "[onActivityResult] requestCode = $requestCode, resultCode = $resultCode")
        when (requestCode) {
            REQ_SELECT_IMG -> {
                Log.w("syTest", "[onActivityResult] intent = $intent, currentImageUri = ${intent?.data}")
                if (resultCode == Activity.RESULT_OK) {
                    val currentImageUri = intent?.data
                    currentImageUri?.let {
                        if (Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                currentImageUri
                            )
                            binding.ivImg.setImageBitmap(bitmap)
                        } else {
                            val source =
                                ImageDecoder.createSource(this.contentResolver, currentImageUri)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            binding.ivImg.setImageBitmap(bitmap)
                        }
                    }
                }
            }

            REQ_CAPTURE_IMG -> {
                if (resultCode == Activity.RESULT_OK) {
                    val extras = intent?.extras
                    val bitmap = extras?.get("data") as Bitmap?
                    Log.w("syTest", "[onActivityResult] intent = $intent, Bitmap W/H = ${bitmap?.width}/${bitmap?.height}")
                    binding.ivImg.setImageBitmap(bitmap)
                }
            }
        }
    }
}