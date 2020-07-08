package br.com.inseguros.ui.quotes.genericscreen.utils

import android.text.Editable
import android.text.TextWatcher
import com.rengwuxian.materialedittext.MaterialEditText

class VehicleLicenceTimeDialogYearTextChanged(private val met: MaterialEditText): TextWatcher {

    override fun afterTextChanged(p0: Editable?) {
        if (p0.toString().isNotEmpty() && p0.toString().toInt() > 0) {
            met.setText("${p0.toString().toInt() * 12}")
        }
        else {
            met.setText("")
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }
}