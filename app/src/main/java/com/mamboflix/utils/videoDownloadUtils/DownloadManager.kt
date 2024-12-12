package com.mamboflix.utils.videoDownloadUtils

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.mamboflix.utils.videoDownloadUtils.DownloadUtil.downloadFileFromUrl
import java.io.File
import okhttp3.OkHttpClient
import okhttp3.Request

class DownloadManager(
    private val downloadListener: DownloadListener?,
    val context: Context,
    val title: String,
    private val userId: String // New parameter for user ID
) : AsyncTask<String?, Int?, Int>() {

    // Flags for download status
    var isDownloadCanceled = false
    var isDownloadPaused = false
    var lastDownloadProgress = 0
    var isDownloadSuccess = false

    private var currDownloadUrl = ""
    private var downloadLocalFile: File? = null

    // OkHttpClient for handling the file download
    private val okHttpClient = OkHttpClient()

    /* This method is invoked after doInBackground() method */
    override fun onPostExecute(downloadStatus: Int) {
        when (downloadStatus) {
            DownloadUtil.DOWNLOAD_SUCCESS -> {
                isDownloadSuccess = true
                isDownloadCanceled = false
                isDownloadPaused = false
                downloadListener?.onSuccess()
            }
            DownloadUtil.DOWNLOAD_FAILED -> {
                isDownloadCanceled = false
                isDownloadPaused = false
                downloadListener?.onFailed()
            }
            DownloadUtil.DOWNLOAD_PAUSED -> {
                downloadListener?.onPaused()
            }
            DownloadUtil.DOWNLOAD_CANCELED -> {
                if (downloadLocalFile?.exists() == true) {
                    downloadLocalFile?.delete()
                }
                downloadListener?.onCanceled()
            }
        }
    }

    /* Invoked when this async task execute. When this method returns, onPostExecute() method will be called. */
    override fun doInBackground(vararg params: String?): Int {
        // Set current thread priority lower than main thread priority to avoid blocking the UI thread
        Thread.currentThread().priority = Thread.NORM_PRIORITY - 2

        var downloadFileUrl = ""
        if (params.isNotEmpty()) {
            downloadFileUrl = params[0] ?: ""
        }

        currDownloadUrl = downloadFileUrl
        downloadLocalFile = createDownloadLocalFile(downloadFileUrl)

        // Proceed with downloading the file
        return downloadFileFromUrl(downloadFileUrl, downloadLocalFile!!)
    }

    // Method to create the local file on the device
    private fun createDownloadLocalFile(downloadFileUrl: String?): File? {
        var ret: File? = null
        try {
            if (downloadFileUrl != null && downloadFileUrl.isNotEmpty()) {
                val lastIndex = downloadFileUrl.lastIndexOf("/")
                if (lastIndex > -1) {
                    val downloadFileName = downloadFileUrl.substring(lastIndex + 1)

                    // Define the directory path based on the userId
                    val downloadDirectory = File(context.filesDir, "Movies/$userId")

                    // Ensure the directory structure exists, if not, create it
                    if (!downloadDirectory.exists()) {
                        val created = downloadDirectory.mkdirs()
                        if (!created) {
                            Log.e("DownloadManager", "Failed to create directory: ${downloadDirectory.absolutePath}")
                        }
                    }

                    // Create the file object in the defined directory
                    ret = File(downloadDirectory, downloadFileName)
                }
            }
        } catch (e: Exception) {

            Log.e("DownloadManager", "Error creating local file", e)
        }
        return ret
    }



    // Method to fetch the file size from the server
    private fun getDownloadUrlFileSize(downloadUrl: String?): Long {
        var fileSize: Long = 0
        try {
            val request = Request.Builder().url(downloadUrl).build()
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val contentLength = response.header("Content-Length")
                fileSize = contentLength?.toLong() ?: 0
            }
        } catch (e: Exception) {
            Log.e("DownloadManager", "Error getting file size", e)
        }
        return fileSize
    }

    // Method to update the download progress
    fun updateTaskProgress(newDownloadProgress: Int) {
        lastDownloadProgress = newDownloadProgress
        downloadListener?.onUpdateDownloadProgress(newDownloadProgress, title)
    }

    // Methods to pause or cancel the download
    fun pauseDownload() {
        isDownloadPaused = true
    }

    fun cancelDownload() {
        isDownloadCanceled = true
    }

    init {
        this.isDownloadPaused = false
        this.isDownloadCanceled = false
    }
}


/*

class DownloadManager(
    downloadListener: DownloadListener?,
    val context: Context,
    val title: String,
    private val userId: String // New parameter for user ID

) : AsyncTask<String?, Int?, Int>() {
    private var downloadListener: DownloadListener? = null
    var isDownloadCanceled = false
    var isDownloadPaused = false
    var lastDownloadProgress = 0
    var isDownloadSuccess=false
    private var currDownloadUrl = ""
    private var downloadLocalFile:File?=null
    */
/* This method is invoked after doInBackground() method. *//*

    override fun onPostExecute(downloadStatue: Int) {
        when (downloadStatue) {
            DownloadUtil.DOWNLOAD_SUCCESS -> {
                isDownloadSuccess=true
                isDownloadCanceled = false
                isDownloadPaused = false
                downloadListener!!.onSuccess()
            }
            DownloadUtil.DOWNLOAD_FAILED -> {
                isDownloadCanceled = false
                isDownloadPaused = false
                downloadListener!!.onFailed()
            }
            DownloadUtil.DOWNLOAD_PAUSED -> {
                downloadListener!!.onPaused()
            }
            DownloadUtil.DOWNLOAD_CANCELED -> {
                if(downloadLocalFile!!.exists()){
                    downloadLocalFile!!.delete()
                }
                downloadListener!!.onCanceled()
            }
        }
    }

    */
/* Invoked when this async task execute.When this method return, onPostExecute() method will be called.*//*

    override fun doInBackground(vararg params: String?): Int  {
        // Set current thread priority lower than main thread priority, so main thread Pause, Continue and Cancel action will not be blocked.
        Thread.currentThread().priority = Thread.NORM_PRIORITY - 2
        var downloadFileUrl = ""
        if (params != null && params.size > 0) {
            downloadFileUrl = params[0]!!
        }
        currDownloadUrl = downloadFileUrl
        downloadLocalFile = createDownloadLocalFile(downloadFileUrl)
        return DownloadUtil.downloadFileFromUrl(downloadFileUrl, downloadLocalFile!!)
    }

    */
/*
     * Parse the download file name from the download file url,
     * check whether the file exist in sdcard download directory or not.
     * If the file do not exist then create it.
     *
     * Return the file object.
     * *//*

    private fun createDownloadLocalFile(downloadFileUrl: String?): File? {
        var ret: File? = null
        try {
            if (downloadFileUrl != null && !TextUtils.isEmpty(downloadFileUrl)) {
                val lastIndex = downloadFileUrl.lastIndexOf("/")
                if (lastIndex > -1) {
                    val downloadFileName = downloadFileUrl.substring(lastIndex + 1)
                    //val downloadFileName = "vohy.swf"
                    //   String downloadDirectoryName = Environment.DIRECTORY_DOWNLOADS;
                    val downloadDirectory = context.filesDir.absolutePath+"/Movies/$userId"
                    val rootFile = File(downloadDirectory)
                    rootFile.mkdir()
                    ret = File(rootFile, downloadFileName)
                }
            }
        } finally {
            return ret
        }
    }

    */
/* Update download async task progress. *//*

    fun updateTaskProgress(newDownloadProgress: Int) {
        lastDownloadProgress = newDownloadProgress
        downloadListener!!.onUpdateDownloadProgress(newDownloadProgress,title)
    }

    fun pauseDownload() {
        isDownloadPaused = true
    }

    fun cancelDownload() {
        isDownloadCanceled = true
    }

    init {
        this.downloadListener = downloadListener
        isDownloadPaused = false
        isDownloadCanceled = false
    }

}*/
