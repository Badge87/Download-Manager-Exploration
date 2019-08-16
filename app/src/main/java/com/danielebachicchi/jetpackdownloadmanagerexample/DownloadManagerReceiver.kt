package com.danielebachicchi.jetpackdownloadmanagerexample

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class DownloadManagerReceiver: BroadcastReceiver() {
    companion object{
        private  val TAG:String? = DownloadManagerReceiver::class.java.simpleName
    }


    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d(
            TAG, String.format("A download with id %s has been completed!",p1?.getLongExtra(
                DownloadManager.EXTRA_DOWNLOAD_ID,-1)))

        Log.d(TAG, p1!!.extras.toString())
    }
}