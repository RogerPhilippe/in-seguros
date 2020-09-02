package br.com.inseguros.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import br.com.inseguros.R
import br.com.inseguros.data.utils.Constants

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle = intent.extras
        if (bundle != null) {

            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
            prefs.edit().putString(Constants.NEW_QUOTATION_PROPOSAL_RECEIVED, bundle["quote_id"] as String).apply()
            prefs.edit().putString(Constants.NEW_PROPOSAL_ID, bundle["proposal_id"] as String ).apply()
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) ==
            PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                1
            )
        }

    }

}