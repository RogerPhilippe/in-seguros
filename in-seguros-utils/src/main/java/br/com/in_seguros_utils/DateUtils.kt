package br.com.in_seguros_utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun convertDateToLong(date: String): Long {
    val df = SimpleDateFormat("dd/MM/yyyy")
    return df.parse(date)!!.time
}

@SuppressLint("SimpleDateFormat")
fun convertDateToString(date: Date): String {
    val df = SimpleDateFormat("dd/MM/yyyy")
    return df.format(date)
}