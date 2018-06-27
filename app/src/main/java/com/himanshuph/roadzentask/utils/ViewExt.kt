package com.himanshuph.roadzentask.utils

import android.support.design.widget.TextInputLayout
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.LinearLayout

fun ViewGroup.inflate(layoutRes: Int) = LayoutInflater.from(context).inflate(layoutRes, this, false)

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun TextInputLayout.getString() : String {
    return editText?.text?.toString()?.trim() ?: run { "" }
}
