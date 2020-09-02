package br.com.in_seguros_utils

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat

fun String.makeShortToast(context: Context) {
    if (this.isNotEmpty()) {
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }
}

fun String.makeErrorShortToast(context: Context) {
    if (this.isNotEmpty()) {
        val toast = Toast.makeText(context, this, Toast.LENGTH_SHORT)
        toast.view?.background?.setTintList(ContextCompat.getColorStateList(context, android.R.color.holo_red_light))
        toast.show()
    }
}