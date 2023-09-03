package com.example.otpapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.otpview.OTPListener
import com.otpview.OTPTextView

class MainActivity : AppCompatActivity() {

    private lateinit var otpTextView : OTPTextView
    private val CAMERA_PERMISSION_CODE = 101
    private val STORAGE_PERMISSION_CODE = 102
    private val REQUEST_IMAGE_PICK = 103

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_PERMISSION_CODE
        )
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        otpTextView = findViewById(R.id.otp_view) as OTPTextView

        val buttonCamera = findViewById<Button>(R.id.button)
        val buttonGallery = findViewById<Button>(R.id.button1)


        otpTextView.requestFocusOTP()
        otpTextView.otpListener = object : OTPListener {
            override fun onInteractionListener() {

            }

            override fun onOTPComplete(otp: String) {
                Toast.makeText(this@MainActivity, "The OTP is $otp", Toast.LENGTH_SHORT).show()
            }
        }

        buttonCamera.setOnClickListener {
            if (!checkCameraPermission()) {
                requestCameraPermission()
            } else {
                // Camera permission is already granted, proceed with capturing photos.
                dispatchTakePictureIntent()
            }
        }
        // For camera access


        buttonGallery.setOnClickListener {
            // For gallery access
            if (!checkStoragePermission()) {
                requestStoragePermission()
            } else {
                // Storage permission is already granted, proceed with accessing the gallery.
                openGallery()
            }

        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Camera permission granted, you can now use the camera.
                } else {
                    // Camera permission denied, handle accordingly (e.g., show a message).
                    showPermissionDeniedDialog()
                }
            }
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Storage permission granted, you can now access the gallery.
                } else {
                    showPermissionDeniedDialog()
                    // Storage permission denied, handle accordingly (e.g., show a message).
                }
            }
        }
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    private val REQUEST_IMAGE_CAPTURE = 101

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun showPermissionDeniedDialog() {
        // Permission was permanently denied, show a message and provide a way to open app settings.
        // You can use AlertDialog to create a message dialog.
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permission Denied")
            .setMessage("Camera permission is required for this feature. Please grant the permission in app settings.")
            .setPositiveButton("Open Settings") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Handle the denial as needed (e.g., show an alternative feature).
            }
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }




}