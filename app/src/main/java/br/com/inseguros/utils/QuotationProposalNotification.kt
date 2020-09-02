package br.com.inseguros.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import br.com.inseguros.R
import java.util.*


object QuotationProposalNotification {

    private const val NOTIFICATION_ID = "QUOTATION_PROPOSAL_NOTIFICATION"
    private const val NOTIFICATION_CHANNEL_ID = "QUOTATION_PROPOSAL_NOTIFICATION_ID"
    private const val NOTIFICATION_CHANNEL_NAME = "QUOTATION_PROPOSAL_NOTIFICATION_CHANNEL"

    private lateinit var mContext: Context
    private lateinit var mContent: Pair<String, String>
    private lateinit var mNotificationManager: NotificationManager

    fun createNotification(context: Context, content: Pair<String, String>) {
        try {
            Objects.requireNonNull(context)
            mContext = context
            mContent = content
            createNotificationChannel()
        } catch (ex: Exception) {
            Log.e("QuoteNotification", ex.message ?: mContext.getString(R.string.unknown_error))
        }
    }

    private fun createNotificationChannel() {

        val messagesChannel: NotificationChannel
        mNotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            messagesChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH)
            messagesChannel.setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            messagesChannel.vibrationPattern = longArrayOf(0, 1000, 1000, 1000, 1000)
            messagesChannel.lightColor = Color.WHITE
            messagesChannel.setShowBadge(true)
            mNotificationManager.createNotificationChannel(messagesChannel)
        }

        showMessageNotification()

    }

    private fun showMessageNotification() {
        val notification = buildNotification()
        mNotificationManager.notify(NOTIFICATION_ID.hashCode(), notification)
    }

    private fun buildNotification(): Notification {

        val builder = NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
        val notification = builder
            .setSmallIcon(R.mipmap.ic_in_seguros_icon)
            .setContentTitle(mContent.first)
            .setContentText(mContent.second)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_ALL)
            .setStyle(NotificationCompat.BigTextStyle().bigText(mContent.second))
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .build()
        notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
        return notification
    }

}