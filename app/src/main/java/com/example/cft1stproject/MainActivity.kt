package com.example.cft1stproject

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    companion object {
        const val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            var builder = StringBuilder()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                    Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                    PERMISSIONS_REQUEST_READ_CONTACTS)
                //callback onRequestPermissionsResult
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ContactFragment.newInstance(this))
                    .commit()
                }
        }
        }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ContactFragment.newInstance(this))
                    .commit()
            } else {
                // toast("Permission must be granted in order to display contacts information")
            }
        }
    }
    }

 /*   override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment is ContactFragment) {
            fragment.onBackPressed()
            return
        }
        super.onBackPressed()
    }*/
