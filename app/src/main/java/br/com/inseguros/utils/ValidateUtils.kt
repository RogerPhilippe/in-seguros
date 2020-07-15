package br.com.inseguros.utils

import android.content.Context
import br.com.inseguros.R
import com.rengwuxian.materialedittext.MaterialEditText

fun validMaterialEditTextFilled(met: MaterialEditText, context: Context): Boolean {
    val errorMsg = context.getString(R.string.error_msg_field_empty)

    if (met.text?.isEmpty() == true) {
        met.error = errorMsg
        return false
    }

    return true
}