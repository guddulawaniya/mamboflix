package com.mamboflix.utils.videoDownloadUtils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.mamboflix.R

class DownloadListener {
    var downloadService: DownloadService? = null
    private var lastProgress = 0
    fun onSuccess() {
        downloadService!!.stopForeground(true)
        sendDownloadNotification("Download success.", -1)
    }

    fun onFailed() {
        downloadService!!.stopForeground(true)
        sendDownloadNotification("Download failed.", -1)
    }

    fun onPaused() {
        sendDownloadNotification("Download paused.", lastProgress)
    }

    fun onCanceled() {
        downloadService!!.stopForeground(true)
       // downloadService.unbindService()
        sendDownloadNotification("Download canceled.", -1)
    }

    fun onUpdateDownloadProgress(progress: Int, title: String) {
        try {
            lastProgress = progress
            sendDownloadNotification(title, progress)

            // Thread sleep 0.2 seconds to let Pause, Continue and Cancel button in notification clickable.
            Thread.sleep(200)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun sendDownloadNotification(title: String?, progress: Int) {
        val notification = getDownloadNotification(title, progress)
        val notificationManager =
            downloadService!!.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

    fun getDownloadNotification(title: String?, progress: Int): Notification {
        val intent = Intent()
        val pendingIntent = PendingIntent.getActivity(downloadService, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "My Background Service")
            } else {
                // For earlier versions, channel ID is not required
                ""
            }

        val notifyBuilder = NotificationCompat.Builder(downloadService!!.applicationContext, channelId)
        notifyBuilder.setSmallIcon(android.R.drawable.stat_sys_download)
        val bitmap = BitmapFactory.decodeResource(downloadService!!.resources, R.drawable.ic_logo)
        notifyBuilder.setLargeIcon(bitmap)
        notifyBuilder.setContentIntent(pendingIntent)
        notifyBuilder.setContentTitle(title)
        notifyBuilder.setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND).setVibrate(longArrayOf(0))
        notifyBuilder.setFullScreenIntent(pendingIntent, true)

        // If the download is in progress
        if (progress in 1..99) {
            notifyBuilder.setContentText("Download progress $progress%")
            notifyBuilder.setProgress(100, progress, false)

            // Add Pause download button intent in notification.
            val pauseDownloadIntent = Intent(downloadService, DownloadService::class.java)
            pauseDownloadIntent.action = DownloadService.ACTION_PAUSE_DOWNLOAD
            val pauseDownloadPendingIntent = PendingIntent.getService(
                downloadService, 0, pauseDownloadIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
            val pauseDownloadAction = NotificationCompat.Action(
                android.R.drawable.ic_media_pause,
                "Pause",
                pauseDownloadPendingIntent
            )
            notifyBuilder.addAction(pauseDownloadAction)

            // Add Cancel download button intent in notification.
            val cancelDownloadIntent = Intent(downloadService, DownloadService::class.java)
            cancelDownloadIntent.action = DownloadService.ACTION_CANCEL_DOWNLOAD
            val cancelDownloadPendingIntent = PendingIntent.getService(
                downloadService, 0, cancelDownloadIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
            val cancelDownloadAction = NotificationCompat.Action(
                android.R.drawable.ic_delete,
                "Cancel",
                cancelDownloadPendingIntent
            )
            notifyBuilder.addAction(cancelDownloadAction)
        }

        // If the download is complete (success)
        else if (title!!.contains("success")) {
            notifyBuilder.setSmallIcon(R.drawable.ic_check_mark)
            notifyBuilder.setContentText("Download complete!")
            notifyBuilder.setProgress(0, 0, false)

            // Optionally, you can add a "Open" button or just let the notification be dismissed
            val openFileIntent = Intent(downloadService, DownloadService::class.java)
            val openFilePendingIntent = PendingIntent.getActivity(
                downloadService, 0, openFileIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val openFileAction = NotificationCompat.Action(
                android.R.drawable.ic_menu_view,
                "Open File",
                openFilePendingIntent
            )
            notifyBuilder.addAction(openFileAction)
        }

        // If the download has been canceled
        else if (title.contains("canceled")) {
            notifyBuilder.setSmallIcon( android.R.drawable.ic_delete)
            notifyBuilder.setContentText("Download canceled")
            notifyBuilder.setProgress(0, 0, false)

            // No need for any actions as the download is canceled
        }

        return notifyBuilder.build()
    }

    /*    fun getDownloadNotification(title: String?, progress: Int): Notification {
            val intent = Intent()
            val pendingIntent = PendingIntent.getActivity(downloadService, 0, intent,
                PendingIntent.FLAG_IMMUTABLE)
            val channelId =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel("my_service", "My Background Service")
                } else {
                    // If earlier version channel ID is not used
                    // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                    ""
                }

            val notifyBuilder = NotificationCompat.Builder(downloadService!!.applicationContext,channelId)
            notifyBuilder.setSmallIcon(android.R.drawable.stat_sys_download)
            val bitmap = BitmapFactory.decodeResource(downloadService!!.resources, R.drawable.ic_logo)
            notifyBuilder.setLargeIcon(bitmap)
            notifyBuilder.setContentIntent(pendingIntent)
            notifyBuilder.setContentTitle(title)
            notifyBuilder.setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND).setVibrate(longArrayOf(0))
            notifyBuilder.setFullScreenIntent(pendingIntent, true)
            if (progress in 1..99) {
                val stringBuffer = StringBuffer()
                stringBuffer.append("Download progress ")
                stringBuffer.append(progress)
                stringBuffer.append("%")
                notifyBuilder.setContentText("Download progress $progress%")
                notifyBuilder.setProgress(100, progress, false)
                // Add Pause download button intent in notification.
                val pauseDownloadIntent = Intent(downloadService, DownloadService::class.java)
                pauseDownloadIntent.action = DownloadService.ACTION_PAUSE_DOWNLOAD
                val pauseDownloadPendingIntent = PendingIntent.getService(
                    downloadService, 0, pauseDownloadIntent, 0
                )
                val pauseDownloadAction = NotificationCompat.Action(
                    android.R.drawable.ic_media_pause,
                    "Pause",
                    pauseDownloadPendingIntent
                )
                notifyBuilder.addAction(pauseDownloadAction)

                // Add Continue download button intent in notification.
                val continueDownloadIntent = Intent(downloadService, DownloadService::class.java)
                continueDownloadIntent.action = DownloadService.ACTION_CONTINUE_DOWNLOAD
                val continueDownloadPendingIntent = PendingIntent.getService(
                    downloadService, 0, continueDownloadIntent, 0
                )
                val continueDownloadAction = NotificationCompat.Action(
                    android.R.drawable.ic_media_play,
                    "Continue",
                    continueDownloadPendingIntent
                )
                notifyBuilder.addAction(continueDownloadAction)

                // Add Cancel download button intent in notification.
                val cancelDownloadIntent = Intent(downloadService, DownloadService::class.java)
                cancelDownloadIntent.action = DownloadService.ACTION_CANCEL_DOWNLOAD
                val cancelDownloadPendingIntent = PendingIntent.getService(
                    downloadService, 0, cancelDownloadIntent, 0
                )
                val cancelDownloadAction = NotificationCompat.Action(
                    android.R.drawable.ic_delete,
                    "Cancel",
                    cancelDownloadPendingIntent
                )
                notifyBuilder.addAction(cancelDownloadAction)
            }
            if(title!!.contains("success")){
                notifyBuilder.setSmallIcon(R.drawable.ic_check_mark)
            }
            return notifyBuilder.build()
        }*/

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_LOW)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        chan.enableVibration(false)
        val notificationManager = downloadService!!.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(chan)
        return channelId
    }
}