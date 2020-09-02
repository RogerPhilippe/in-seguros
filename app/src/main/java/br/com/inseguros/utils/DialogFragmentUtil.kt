package br.com.inseguros.utils

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import br.com.inseguros.R

class DialogFragmentUtil(
    var msg: String,
    var yesBtn: () -> Unit,
    var noBtn: () -> Unit
): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(msg)
                .setPositiveButton(getString(R.string.yes_btn_label)) { _, _ -> yesBtn() }
                .setNegativeButton(getString(R.string.no_btn_label)) { _, _ -> noBtn() }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null.")
    }

}