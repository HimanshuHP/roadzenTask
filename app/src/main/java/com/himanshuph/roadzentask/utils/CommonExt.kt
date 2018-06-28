package com.himanshuph.roadzentask.utils

import android.content.Context
import android.widget.Toast

fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) = this?.let { Toast.makeText(it, text, duration).show() }