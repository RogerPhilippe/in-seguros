package br.com.inseguros.service

import android.util.Log
import androidx.preference.PreferenceManager
import br.com.inseguros.data.AppSession
import br.com.inseguros.data.utils.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseCloudMsgService: FirebaseMessagingService() {

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        if (newToken.isNotEmpty()) {
            Log.i("NEW_TOKEN", newToken)
            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
            prefs.edit().putString(Constants.NEW_TOKEN_KEY, newToken).apply()
            AppSession.setCurrentMessagingServiceNewToken(newToken)
        } else {
            Log.e("NEW_TOKEN", "IsEmpty!")
        }
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        val dataSize = p0.data.size
        Log.e("MSG_RECEIVED", dataSize.toString())
    }

}