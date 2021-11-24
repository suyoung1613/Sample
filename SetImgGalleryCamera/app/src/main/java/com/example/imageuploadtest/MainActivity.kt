package com.example.imageuploadtest

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import com.example.imageuploadtest.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.tvGallery.setOnClickListener { pickImg() }
        binding.tvCamera.setOnClickListener { picture() }
        binding.tvCameraOriginSize.setOnClickListener { pictureWithOriginSize() }
    }

    /**
     * 1)갤러리 이미지 가져오기
     */
    private fun pickImg() {
        startActivityForResult(pickIntent, REQ_SELECT_IMG)
    }

    /**
     * 2)카메라 촬영 이미지 가져오기
     *
     * 단순히 사진 촬영 해서
     */
    private fun picture() {
        pictureIntent.resolveActivity(packageManager)?.also {
            Log.d("syTest", "ComponentName =$it")
            startActivityForResult(intent, REQ_IMG_CAPTURE)
        }
    }

    /**
     * 3)카메라 촬영 이미지 (원본 사이즈로 가져오기)
     */
    private fun pictureWithOriginSize() {
        pictureIntent.resolveActivity(packageManager)?.also {
            startActivityForResult(
                IntentMaker.getFullSizePictureIntent(applicationContext),
                REQ_IMG_CAPTURE_FULL_SIZE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        Log.w("syTest", "[onActivityResult] requestCode = $requestCode, resultCode = $resultCode")
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQ_SELECT_IMG -> {
                    Log.w(
                        "syTest",
                        "[onActivityResult] intent = $intent, currentImageUri = ${intent?.data}"
                    )
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

                REQ_IMG_CAPTURE -> {
                    val extras = intent?.extras
                    val bitmap = extras?.get("data") as Bitmap?
                    Log.w(
                        "syTest",
                        "[onActivityResult] intent = $intent, Bitmap W/H = ${bitmap?.width}/${bitmap?.height}"
                    )
                    // 얻어온 bitmap은 썸네일 사용 정도의 크기로 resize 된 이미지
                    // Bitmap W/H = 189/252 언저리로 나옴
                    binding.ivImg.setImageBitmap(bitmap)
                }

                REQ_IMG_CAPTURE_FULL_SIZE -> {
                    setPicFromFile()
                }
            }
        }
    }

    private fun setPicFromFile() {
        // Get the dimensions of the View
        val targetW: Int = binding.ivImg.width
        val targetH: Int = binding.ivImg.height

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
            inPurgeable = true
        }
        if(!TextUtils.isEmpty(IntentMaker.currentPhotoPath)){
            BitmapFactory.decodeFile(IntentMaker.currentPhotoPath, bmOptions)?.also { bitmap ->
                binding.ivImg.setImageBitmap(bitmap)
            }
        }else{
            //TODO ERR
        }
    }
    /*private fun chooser() {
        Intent.createChooser(pickIntent, "사진을 가져올 앱을 선택해 주세요").run { // pickIntent 추가
            if (pictureIntent.resolveActivity(packageManager) != null) {//pictureIntent 추가
                putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pictureIntent))
            }
            startActivityForResult(this, REQ_GET_IMG)
        }
    }*/
}


