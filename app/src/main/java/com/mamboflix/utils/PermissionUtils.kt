package com.mamboflix.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


object PermissionUtils {

    val PERMISSION_REQUEST_CODE = 100


    fun checkPermissionStorage(context: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        return if (result == PackageManager.PERMISSION_GRANTED) {

            if (result1 == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                false
            }


        } else {
            false

        }
    }

    fun checkPermissionLocation(context: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        return if (result == PackageManager.PERMISSION_GRANTED) {

            if (result1 == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                false
            }


        } else {
            false

        }
    }


    fun checkPermissionCall(context: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
        return if (result == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            false
        }
    }

    fun checkPermissionReadContact(context: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
        return if (result == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            false
        }
    }

    fun checkPermissionAccount(context: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS)
        return if (result == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            false
        }
    }


    fun checkPermissionContect(context: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS)
        return if (result == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            false

        }
    }

    fun requestPermissionLocation(activity: Activity) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_CODE)
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)

        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_CODE)
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
        }

    }


    fun requestPermissionContect(activity: Activity) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_CONTACTS) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_CONTACTS), PERMISSION_REQUEST_CODE)
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_CONTACTS), PERMISSION_REQUEST_CODE)
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CALL_PHONE), PERMISSION_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_CONTACTS), PERMISSION_REQUEST_CODE)
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_CONTACTS), PERMISSION_REQUEST_CODE)
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CALL_PHONE), PERMISSION_REQUEST_CODE)
        }

    }

    fun requestPermissionAccount(activity: Activity) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.GET_ACCOUNTS) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.GET_ACCOUNTS_PRIVILEGED)) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.GET_ACCOUNTS_PRIVILEGED), PERMISSION_REQUEST_CODE)
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.GET_ACCOUNTS), PERMISSION_REQUEST_CODE)

        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.GET_ACCOUNTS_PRIVILEGED), PERMISSION_REQUEST_CODE)
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.GET_ACCOUNTS), PERMISSION_REQUEST_CODE)
        }

    }


    fun requestPermissionCall(activity: Activity) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CALL_PHONE), PERMISSION_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CALL_PHONE), PERMISSION_REQUEST_CODE)
        }

    }


    fun requestPermissionAudio(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAPTURE_AUDIO_OUTPUT), PERMISSION_REQUEST_CODE)
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.MODIFY_AUDIO_SETTINGS), PERMISSION_REQUEST_CODE)
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.RECORD_AUDIO), PERMISSION_REQUEST_CODE)
    }

    fun requestPermissionStorage(activity: Activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }
    }

    fun checkPermissionCamera(context: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        return if (result == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            false

        }
    }

    fun checkPermissionAudio(context: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
        return if (result == PackageManager.PERMISSION_GRANTED) {

            true

        } else {
            false

        }
    }

    fun requestCameraPermission(activity: Activity, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: Int) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)

        }
    }

}
