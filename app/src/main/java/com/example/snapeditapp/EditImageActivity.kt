package com.example.snapeditapp

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.snapeditapp.databinding.ActivityEditImageBinding
import jp.co.cyberagent.android.gpuimage.GPUImageView
import jp.co.cyberagent.android.gpuimage.filter.GPUImageExposureFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHueFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageMonochromeFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSketchFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditImageBinding
    private var imageUri: Uri? = null
    private lateinit var gpuImageView: GPUImageView
    private val filters = listOf(
        GPUImageFilter(),
        GPUImageExposureFilter(),
        GPUImageGrayscaleFilter(),
        GPUImageSketchFilter(),
        GPUImageMonochromeFilter(),
        GPUImageHueFilter()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageArrowBack.setOnClickListener {
            onBackPressed()
        }

        binding.imageSave.setOnClickListener {
            saveImage()
        }

        gpuImageView = binding.gpuImageView

        imageUri = intent.getParcelableExtra("imageUri")
        val bitmap = getBitmapFromUri(imageUri!!)
        gpuImageView.setImage(bitmap)
        setupRecyclerView(bitmap)
    }

    private fun setupRecyclerView(originalBitmap: Bitmap) {
        val imageAdapter = EditImageAdapter(filters, originalBitmap) { filter ->
            applyFilter(filter)
        }
        binding.filtersRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@EditImageActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = imageAdapter
            setHasFixedSize(true)
        }
    }

    private fun applyFilter(filter: GPUImageFilter) {
        gpuImageView.filter = filter
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val inputStream = contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
            ?: throw IllegalArgumentException("Bitmap could not be decoded")
    }

    private fun saveImage() {
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap =
                gpuImageView.capture(gpuImageView.width, gpuImageView.height) ?: return@launch

            val contentValues = ContentValues().apply {
                put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    "filtered_image_${System.currentTimeMillis()}.jpg"
                )
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Edited")
            }

            val uri =
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let {
                contentResolver.openOutputStream(it)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@EditImageActivity,
                        "Image saved successfully in Gallery!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

