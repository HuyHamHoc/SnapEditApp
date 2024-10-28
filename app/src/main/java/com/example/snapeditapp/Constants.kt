package com.example.snapeditapp

import android.Manifest
import android.os.Build

object Constants {
    const val TAG = "CameraXApp"
    const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    val REQUIRED_PERMISSIONS = mutableListOf(
        Manifest.permission.CAMERA,
    ).apply {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }.toTypedArray()
}