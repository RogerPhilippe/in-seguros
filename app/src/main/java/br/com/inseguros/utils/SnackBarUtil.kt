package br.com.inseguros.utils

import android.app.Activity
import br.com.inseguros.R
import com.google.android.material.snackbar.Snackbar

class SnackBarUtil {

    fun create(activity: Activity,
               msg: String,
               action: () -> Unit
    ) {
        val snackBar = Snackbar.make(activity.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG)
        snackBar.setAction(activity.getString(R.string.undo_label)) { action() }
        snackBar.show()
    }

}
