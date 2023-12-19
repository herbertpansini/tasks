package com.ztec.tasks.service.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ztec.tasks.R
import com.ztec.tasks.service.constants.TaskConstants
import com.ztec.tasks.service.repository.SecurityPreferences
import com.ztec.tasks.view.MainActivity

class FCMService : FirebaseMessagingService() {

    private val securityPreferences by lazy { SecurityPreferences(this) }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.body.let {
            sendNotification(it)
        }
    }

    override fun onNewToken(deviceToken: String) {
        sendRegistrationToServer(deviceToken)
    }

    private fun sendRegistrationToServer(deviceToken: String) {
        securityPreferences.store(TaskConstants.USER.DEVICE_TOKEN, deviceToken)
    }

    private fun sendNotification(messageBody: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_done)
                .setContentTitle(getString(R.string.fcm_message))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }
}