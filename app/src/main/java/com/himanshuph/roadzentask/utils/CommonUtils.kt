package com.himanshuph.roadzentask.utils

import android.content.Context
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