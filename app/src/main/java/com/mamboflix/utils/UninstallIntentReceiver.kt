package com.mamboflix.utils

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.util.Log
import android.widget.Toast
import java.io.File

class UninstallIntentReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        // fetching package names from extras
        val packageNames = intent.getStringArrayExtra("android.intent.extra.PACKAGES")
        if (packageNames != null) {
            for (packageName in packageNames) {
                if (packageName != null && packageName == "com.mamboflix") {
                    ListenActivities(context).start()
                }
            }
        }
    }

    internal class ListenActivities(con: Context?) : Thread() {
        var exit = false
        var am: ActivityManager? = null
        var context: Context? = null
        override fun run() {
            Looper.prepare()
            while (!exit) {

                // get the info from the currently running task
                val taskInfo = am!!.getRunningTasks(MAX_PRIORITY)
                val activityName = taskInfo[0].topActivity!!.className
                Log.d("topActivity", "CURRENT Activity ::"
                        + activityName)
                if (activityName == "com.android.packageinstaller.UninstallerActivity") {
                    val dir = File(context!!.filesDir.absolutePath.toString()+"/Movies")
                    if (dir.isDirectory()) {
                        val children: Array<out String>? = dir.list()
                        for (i in children!!.indices) {
                            File(dir, children[i]).delete()
                        }
                    }
                    exit = true
                    Toast.makeText(context, "Done with preuninstallation tasks... Exiting Now", Toast.LENGTH_SHORT).show()
                } else if (activityName == "com.android.settings.ManageApplications") {
                    exit = true
                }
            }
            Looper.loop()
        }

        init {
            context = con
            am = context!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        }
    }
}