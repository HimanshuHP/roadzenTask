package com.himanshuph.roadzentask.utils

import android.Manifest
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import com.himanshuph.roadzentask.R

fun Fragment.requestReadFilePermission(requestCode: Int,dialogCallbackInterface: DialogCallbackInterface) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE))
        showRationaleDialog(activity, getString(R.string.photo_permission_rationale), Manifest.permission.READ_EXTERNAL_STORAGE, dialogCallbackInterface)
    else
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), requestCode)
}

fun Fragment.requestLocationPermission(requestCode: Int,dialogCallbackInterface: DialogCallbackInterface) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION))
        showRationaleDialog(activity, getString(R.string.location_permission_rationale), Manifest.permission.ACCESS_FINE_LOCATION, dialogCallbackInterface)
    else
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), requestCode)
}