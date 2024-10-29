package com.example.snapeditapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
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

        binding.imageArrowBack.setOnClickListener{
            onBackPressed()
        }

        gpuImageView = binding.gpuImageView

        imageUri = intent.getParcelableExtra("imageUri")
        if (imageUri != null) {
            val bitmap = getBitmapFromUri(imageUri!!)
            gpuImageView.setImage(bitmap)
            setupRecyclerView(bitmap)
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupRecyclerView(originalBitmap: Bitmap) {
        val imageAdapter = EditImageAdapter(filters, originalBitmap) { filter ->
            applyFilter(filter)
        }
        binding.filtersRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@EditImageActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = imageAdapter
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
}
