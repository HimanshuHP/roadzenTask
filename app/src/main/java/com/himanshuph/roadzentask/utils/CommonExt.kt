package com.himanshuph.roadzentask.utils

import android.content.Context
import android.location.LocationManager
import android.widget.Toast

fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) = this?.let { Toast.makeText(it, text, duration).show() }

fun Context?.isLocationEnabled(): Boolean {
    val manager = this?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}