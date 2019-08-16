package com.danielebachicchi.jetpackdownloadmanagerexample

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object{
        private const val TAG:String= "MainActivity"
        //is http call so we must add in manifest android:usesCleartextTraffic="true".. Better not to do
        // in a true app :)
        // In this case is ok because is a test app but remember, don't do in your REAL app
        // Only https call if you can!
        private const val DEFAULT_DOWNLOAD_LINK = "http://ipv4.download.thinkbroadband.com/100MB.zip"
    }

    private lateinit var manager:DownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        txt_url.editText!!.setText( DEFAULT_DOWNLOAD_LINK)
        layout_n_feature.visibility = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) View.VISIBLE else View.GONE
        txt_notification_description.editText?.setText(getString(R.string.default_notification_description))
        txt_notification_title.editText?.setText(getString(R.string.default_notification_title))


        btn_download.setOnClickListener { enqueueDownload(generateDownloadRequest())}


    }

    private fun enqueueDownload(request:DownloadManager.Request){
        val id:Long = manager.enqueue(request)
        Log.d(TAG, String.format("New Download Enqueued with id -> %s", id))
    }

    private fun generateDownloadRequest():DownloadManager.Request{
        val request:DownloadManager.Request = DownloadManager.Request(Uri.parse(txt_url.editText?.text.toString()))
        request.setTitle(txt_notification_title.editText?.text.toString())
        request.setDescription(txt_notification_description.editText?.text.toString())

        request.setAllowedOverMetered(switch_allow_mobile.isChecked)


        request.setAllowedOverRoaming(switch_allow_roaming.isChecked)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            request.setRequiresCharging(switch_require_charge.isChecked)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            request.setRequiresDeviceIdle(switch_require_idle.isChecked)
        }
        if(switch_notification_visible.isChecked)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        return request
    }


}
