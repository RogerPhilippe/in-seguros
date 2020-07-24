package br.com.inseguros.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

    }

}