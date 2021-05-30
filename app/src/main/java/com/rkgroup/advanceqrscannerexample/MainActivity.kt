package com.rkgroup.advanceqrscannerexample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.rkgroup.advanceqrscanner.Scanner
import com.rkgroup.advanceqrscanner.DecodeCallback

class MainActivity : AppCompatActivity() {
    private lateinit var scanner: Scanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scanner = Scanner(
            this,
            findViewById(R.id.scannerView)
        )
        scanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                Toast.makeText(this, it.text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        scanner.onResume()
    }

    override fun onPause() {
        scanner.onPause()
        super.onPause()

    }

    override fun onDestroy() {
        scanner.onDestroy()
        super.onDestroy()
    }

}