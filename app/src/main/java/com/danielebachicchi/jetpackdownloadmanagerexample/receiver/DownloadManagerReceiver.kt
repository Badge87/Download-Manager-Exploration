package com.danielebachicchi.jetpackdownloadmanagerexample.receiver

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.danielebachicchi.jetpackdownloadmanagerexample.utils.DownloadManagerUtils
import com.danielebachicchi.jetpackdownloadmanagerexample.item.DownloadObject
import com.danielebachicchi.jetpackdownloadmanagerexample.view.DownloadObjectDialogFragment

class DownloadManagerReceiver: BroadcastReceiver() {
    companion object{
        private  val TAG:String? = DownloadManagerReceiver::class.java.simpleName
    }


    override fun onReceive(p0: Context?, p1: Intent?) {
        val action:String? = p1?.action
        Log.d(TAG,String.format("onReceive Intent Action -> %s", action))

        var downloadID:Long = -1

        if(DownloadManager.ACTION_DOWNLOAD_COMPLETE == action){
            downloadID = p1.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1)
            val downloadManager =
                DownloadManagerUtils.getSystemDownloadManager(
                    p0!!
                )
            Toast.makeText(p0, String.format("A download with id %s has been completed!", downloadID), Toast.LENGTH_SHORT).show()


            val downloadObject: DownloadObject? =
                DownloadManagerUtils.queryDownloadManagerObject(
                    downloadManager,
                    downloadID
                )
            Log.d(TAG, downloadObject.toString())

        }else if (DownloadManager.ACTION_NOTIFICATION_CLICKED == action){

            downloadID = p1.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1)

        }

        if(downloadID >= 0)
            launchApp(p0!!, downloadID)

    }

   private fun launchApp(context:Context,idDownloadObject:Long){

        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName) ?: return

        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or
        Intent.FLAG_ACTIVITY_NEW_TASK or
        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_SINGLE_TOP


        //intent.action = Intent.ACTION_MAIN;
        //intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.putExtra(DownloadObjectDialogFragment.EXTRA_DOWNLOAD_OBJECT_ID, idDownloadObject)

        context.startActivity(intent)
    }
}