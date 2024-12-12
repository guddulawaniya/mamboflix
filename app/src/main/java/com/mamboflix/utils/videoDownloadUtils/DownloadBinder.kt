package com.mamboflix.utils.videoDownloadUtils

import android.content.Context
import android.os.Binder
import android.text.TextUtils
import java.io.File

class DownloadBinder(val context: Context) : Binder() {
    var downloadManager: DownloadManager? = null
        private set
    var downloadListener: DownloadListener? = null
    private var currDownloadUrl: String? = ""
    private var downloadPath: String? = null  // Store the download path


    /*    fun startDownload(downloadUrl: String?, title: String, progress: Int) {
            *//* Because downloadManager is a subclass of AsyncTask, and AsyncTask can only be executed once,
         * So each download need a new downloadManager. *//*
        downloadManager = DownloadManager(downloadListener,context,title)

        *//* Because DownloadUtil has a static variable of downloadManger, so each download need to use new downloadManager. *//*DownloadUtil.downloadManager =
            downloadManager

        // Execute download manager, this will invoke downloadManager's doInBackground() method.
        downloadManager!!.execute(downloadUrl)

        // Save current download file url.
        currDownloadUrl = downloadUrl

        // Create and start foreground service with notification.
        val notification = downloadListener!!.getDownloadNotification(title, progress)
        downloadListener!!.downloadService!!.startForeground(1, notification)
    }*/
    fun startDownload(downloadUrl: String?, title: String, progress: Int,userId: String) {


        // Create a new DownloadManager with the download path for this user
        downloadManager = DownloadManager(downloadListener, context, title, userId)

        // Update the DownloadUtil's static downloadManager
        DownloadUtil.downloadManager = downloadManager

        // Execute the download
        downloadManager!!.execute(downloadUrl)

        // Save current download file URL
        currDownloadUrl = downloadUrl

        // Create and start the foreground service with a notification
        val notification = downloadListener!!.getDownloadNotification(title, progress)
        downloadListener!!.downloadService!!.startForeground(1, notification)
    }


    fun setDownloadPathForUser(userId: String) {
        // Create a directory path based on user ID, ensuring it's within the app's internal storage
        downloadPath = context.filesDir.absolutePath + "/Movies/$userId"

        // Ensure the directory exists (create it if it doesn't)
        val directory = File(downloadPath)
        if (!directory.exists()) {
            directory.mkdirs()  // Create the directory for the user
        }
    }

    fun continueDownload() {
        if (currDownloadUrl != null && !TextUtils.isEmpty(currDownloadUrl)) {
            val lastDownloadProgress = downloadManager!!.lastDownloadProgress
            startDownload(currDownloadUrl, "", lastDownloadProgress,"")
        }
    }

    fun cancelDownload() {
        downloadManager!!.cancelDownload()
    }

    fun pauseDownload() {
        downloadManager!!.pauseDownload()
    }

    init {
        if (downloadListener == null) {
            downloadListener = DownloadListener()
        }
    }

}