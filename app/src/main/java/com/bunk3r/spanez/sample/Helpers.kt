package com.bunk3r.spanez.sample

import android.content.Context
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

@ColorInt
fun Context.color(@ColorRes resId: Int): Int {
    return ContextCompat.getColor(this, resId)
}
