package com.mamboflix.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.mamboflix.R
import com.mamboflix.prefs.UserPref
import com.mamboflix.ui.activity.NotificationsActivity
import org.json.JSONObject


class MyFirebaseMessagingService : FirebaseMessagingService() {

    lateinit var userPref:UserPref
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        userPref = UserPref(this)
        if(userPref.getNotification()){
            Log.d(TAG, "onMessageReceived: " + remoteMessage.data)
            if (remoteMessage.data.isNotEmpty()) {
                val gSon = Gson()
                val jsonObject = JSONObject(remoteMessage.data as Map<*, *>)
                val firebasePushResponse: FirebasePushResponse = gSon.fromJson<FirebasePushResponse>(
                    jsonObject.toString(),
                    FirebasePushResponse::class.java
                )
                userPref.setNotificationdot(true)
                sendNotification(firebasePushResponse)

            } else {
                val gSon = Gson()
                val jsonObject = JSONObject(remoteMessage.data as Map<*, *>)
                val firebasePushResponse: FirebasePushResponse = gSon.fromJson<FirebasePushResponse>(
                    jsonObject.toString(),
                    FirebasePushResponse::class.java
                )
                firebasePushResponse.title =
                    remoteMessage.notification!!.title!!
                getString(R.string.app_name)
                userPref.setNotificationdot(true)
                sendNotification(firebasePushResponse)
            }
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    var NOTIFICATION_ID: Int = 234

    private fun sendNotification(messageBody: FirebasePushResponse) {
        val CHANNEL_ID = "my_channel_01"
        val intent = Intent(this, NotificationsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val bitmap = BitmapFactory.decodeFile("https://mamboflix.tv/mamboflixapi/storage/app//content_images/16032417105926862vendor.png")

        Log.d(TAG, "sendNotification: ")
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(messageBody.title)
            .setContentText(messageBody.body)
            .setLargeIcon(bitmap)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }
}