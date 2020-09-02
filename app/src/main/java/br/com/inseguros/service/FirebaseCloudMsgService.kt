package br.com.inseguros.service

import android.util.Log
import androidx.preference.PreferenceManager
import br.com.inseguros.data.sessions.AppSession
import br.com.inseguros.data.utils.Constants
import br.com.inseguros.events.NotifyQuotationReceivedEvent
import br.com.inseguros.utils.QuotationProposalNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.greenrobot.eventbus.EventBus

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

        if (dataSize > 0) {

            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
            prefs.edit().putString(Constants.NEW_QUOTATION_PROPOSAL_RECEIVED, p0.data["quote_id"]).apply()
            prefs.edit().putString(Constants.NEW_PROPOSAL_ID, p0.data["proposal_id"]).apply()

            QuotationProposalNotification.createNotification(
                this,
                Pair(p0.data["title"] ?: "", p0.data["bodyText"] ?: "")
            )
        }

        Log.e("MSG_RECEIVED", dataSize.toString())

        EventBus.getDefault().post(NotifyQuotationReceivedEvent())

    }

}