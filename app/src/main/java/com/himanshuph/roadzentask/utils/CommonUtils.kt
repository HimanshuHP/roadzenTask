package com.himanshuph.roadzentask.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import com.himanshuph.roadzentask.R
import java.io.IOException
import java.nio.charset.Charset

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
    fun onOkClick()
    fun onCancelClick()
}

fun showRationaleDialog(context: Context, msg: String, dialogCallbackInterface: DialogCallbackInterface) {
    AlertDialog.Builder(context, R.style.AlertDialogCustom)
            .setMessage(msg)
            .setPositiveButton(context.getString(android.R.string.ok)) { dialogInterface, i -> dialogCallbackInterface.onOkClick() }
            .setNegativeButton(context.getString(android.R.string.cancel)) { dialogInterface, i -> dialogCallbackInterface.onCancelClick() }
            .create()
            .show()
}