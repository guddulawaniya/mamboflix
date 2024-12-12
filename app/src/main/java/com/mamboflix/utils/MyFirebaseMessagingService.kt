package com.mamboflix.utils

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.mamboflix.R

import kotlin.random.Random

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseMessagingServices : FirebaseMessagingService() {

    private var count = 0
    var notificationCount = 0

    override fun onMessageReceived(remote: RemoteMessage) {
        super.onMessageReceived(remote)

        Log.i(
            "pushNotification", Gson().toJson(remote.data)
        )
        Log.e("msg", "msg")
        val data = remote.data
        Log.e("noti_message_data", data.toString())
        val title = remote.notification?.title ?: "title"
        val body = remote.notification?.body ?: "body"
        createNotification(body, remote.data)
    }

    private fun createNotification(
        body1: String,
        data: MutableMap<String, String>
    ) {
        val title = data["body"].toString()
        val type: String = data["type"].toString()
        val id: String = data["id"].toString()
        Log.d("TAG", "createNotification: "+Gson().toJson(data))



        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
            .setSmallIcon(getNotificationIcon())
            .setSound(defaultSoundUri)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setChannelId(getString(R.string.default_notification_channel_id))
        val notificationBuilder1 =
            NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))

                .setSmallIcon(getNotificationIcon())
                .setSound(defaultSoundUri)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setContentIntent(contentIntent)
                .setChannelId(getString(R.string.default_notification_channel_id))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(getString(R.string.default_notification_channel_id))

            val notificationChannel = NotificationChannel(
                getString(R.string.default_notification_channel_id),
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            notificationManager?.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(Random.nextInt(), notificationBuilder.build())
        // count += 1

    }

    private fun getNotificationIcon(): Int {

        val useWhiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        return if (useWhiteIcon) R.mipmap.ic_launcher_round else R.mipmap.ic_launcher_round
    }

}