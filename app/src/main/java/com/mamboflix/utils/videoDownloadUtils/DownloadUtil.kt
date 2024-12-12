package com.mamboflix.utils.videoDownloadUtils

import android.text.TextUtils
import android.util.Log
import com.mamboflix.ui.activity.contentdetails.ContentDetailsActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream

object DownloadUtil {
    const val TAG_DOWNLOAD_MANAGER = "TAG_DOWNLOAD_MANAGER"
    const val DOWNLOAD_SUCCESS = 1
    const val DOWNLOAD_FAILED = 2
    const val DOWNLOAD_PAUSED = 3
    const val DOWNLOAD_CANCELED = 4
    var downloadManager: DownloadManager? = null
    var contentDetailsActivity: ContentDetailsActivity? = null
    private val okHttpClient = OkHttpClient()

    /* Get download file size returned from http server header. */
    private fun getDownloadUrlFileSize(downloadUrl: String?): Long {
        var ret: Long = 0
        try {
            if (downloadUrl != null && !TextUtils.isEmpty(downloadUrl)) {
                var builder = Request.Builder()
                builder = builder.url(downloadUrl)
                val request = builder.build()
                val call = okHttpClient.newCall(request)
                val response = call.execute()
                if (response != null) {
                    if (response.isSuccessful) {
                        val contentLength = response.header("Content-Length")
                        ret = contentLength!!.toLong()
                    }
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG_DOWNLOAD_MANAGER, ex.message, ex)
        } finally {
            return ret
        }
    }

    fun downloadFileFromUrl(downloadFileUrl: String?, existLocalFile: File): Int {
        var ret = DOWNLOAD_SUCCESS
        try {
            val downloadFileLength = getDownloadUrlFileSize(downloadFileUrl)
            val existLocalFileLength = existLocalFile.length()
            if (downloadFileLength == 0L) {
                ret = DOWNLOAD_FAILED
            } else if (downloadFileLength == existLocalFileLength) {
                ret = DOWNLOAD_SUCCESS
            } else {
                var builder = Request.Builder()
                builder = builder.url(downloadFileUrl)
                builder = builder.addHeader("RANGE", "bytes=$existLocalFileLength")
                val request = builder.build()
                val call = okHttpClient.newCall(request)
                val response = call.execute()
                if (response != null && response.isSuccessful) {

                    val f = FileOutputStream(existLocalFile)
                    val responseBody = response.body()
                    val inputStream = responseBody!!.byteStream()
                    val bufferedInputStream = BufferedInputStream(inputStream)
                    val data = ByteArray(102400)
                    var totalReadLength: Long = 0
                    var readLength = bufferedInputStream.read(data)
                    while (readLength != -1) {
                        if (downloadManager!!.isDownloadPaused) {
                            ret = DOWNLOAD_PAUSED
                            break
                        } else if (downloadManager!!.isDownloadCanceled) {
                            ret = DOWNLOAD_CANCELED
                            break
                        } else {
                            f.write(data, 0, readLength)
                            totalReadLength += readLength
                            val downloadProgress =
                                ((totalReadLength + existLocalFileLength) * 100 / downloadFileLength).toInt()

                            downloadManager!!.updateTaskProgress(downloadProgress)
                            readLength = bufferedInputStream.read(data)
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG_DOWNLOAD_MANAGER, ex.message, ex)
        } finally {
            return ret
        }
    }
}