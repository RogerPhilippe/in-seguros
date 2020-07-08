package br.com.inseguros.utils

import android.content.Context
import android.widget.Toast

fun String.makeShortToast(context: Context) {
    if (this.isNotEmpty()) {
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }
}