package com.himanshuph.roadzentask.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.location.Address
import android.location.Geocoder
import android.support.v7.app.AlertDialog
import com.google.android.gms.maps.model.LatLng
import com.himanshuph.roadzentask.R
import java.io.IOException
import java.nio.charset.Charset
import java.util.*

@Throws(IOException::class)
fun loadJSONFromAsset(context: Context, jsonFileName: String): String {

    val manager = context.assets
    val `is` = manager.open(jsonFileName)

    val size = `is`.available()
    val buffer = ByteArray(size)
    `is`.read(buffer)
    `is`.close()

    return String(buffer, Charset.forName("UTF-8"))
}

interface DialogCallbackInterface {
    fun onOkClick(permission: String)
    fun onCancelClick(permission: String)
}

fun showRationaleDialog(context: Context, msg: String,permission: String, dialogCallbackInterface: DialogCallbackInterface) {
    AlertDialog.Builder(context, R.style.AlertDialogCustom)
            .setMessage(msg)
            .setPositiveButton(context.getString(android.R.string.ok)) { dialogInterface, i -> dialogCallbackInterface.onOkClick(permission) }
            .setNegativeButton(context.getString(android.R.string.cancel)) { dialogInterface, i -> dialogCallbackInterface.onCancelClick(permission) }
            .create()
            .show()
}

fun getAddress(context: Context, latestPosition: LatLng?): Address? {
    val geocoder = Geocoder(context.applicationContext, Locale.getDefault())
    val addresses: List<Address>? = geocoder.getFromLocation(latestPosition?.latitude
            ?: 0.0, latestPosition?.longitude
            ?: 0.0, 1)
    return if (addresses != null && addresses.isNotEmpty()) {
        addresses[0]
    } else null
}