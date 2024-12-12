package com.mamboflix.utils.videoDownloadUtils

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

class DownloadService : Service() {
    private val downloadBinder = DownloadBinder(this)
    override fun onBind(intent: Intent?): IBinder {
        downloadBinder.downloadListener!!.downloadService = this
        return downloadBinder
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent!!.action) {
            ACTION_PAUSE_DOWNLOAD -> {
                downloadBinder.pauseDownload()
                Toast.makeText(applicationContext, "Download is paused", Toast.LENGTH_LONG).show()
            }
            ACTION_CANCEL_DOWNLOAD -> {
                downloadBinder.cancelDownload()
                Toast.makeText(applicationContext, "Download is canceled", Toast.LENGTH_LONG).show()
            }
            ACTION_CONTINUE_DOWNLOAD -> {
                downloadBinder.continueDownload()
                Toast.makeText(applicationContext, "Download continue", Toast.LENGTH_LONG).show()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    companion object {
        const val ACTION_PAUSE_DOWNLOAD = "ACTION_PAUSE_DOWNLOAD"
        const val ACTION_CONTINUE_DOWNLOAD = "ACTION_CONTINUE_DOWNLOAD"
        const val ACTION_CANCEL_DOWNLOAD = "ACTION_CANCEL_DOWNLOAD"
    }
}