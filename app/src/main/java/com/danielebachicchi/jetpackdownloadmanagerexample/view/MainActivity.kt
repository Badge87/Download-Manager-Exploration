package com.danielebachicchi.jetpackdownloadmanagerexample.view

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.danielebachicchi.jetpackdownloadmanagerexample.utils.DownloadManagerUtils
import com.danielebachicchi.jetpackdownloadmanagerexample.R
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {
    companion object{

        //PREFERENCES KEYS
        private const val PREFERENCE_KEY_URL = "download_url"
        private const val PREFERENCE_KEY_ALLOW_MOBILE = "allow_mobile"
        private const val PREFERENCE_KEY_ALLOW_ROAMING = "allow_roaming"
        private const val PREFERENCE_KEY_SHOW_NOTIFICATION = "show_notification"
        private const val PREFERENCE_KEY_TITLE = "download_title"
        private const val PREFERENCE_KEY_DESCRIPTION = "download_description"
        private const val PREFERENCE_KEY_REQUIRE_CHARGE = "require_charge"
        private const val PREFERENCE_KEY_REQUIRE_IDLE = "require_idle"


        //it is a http call so we must add in manifest android:usesCleartextTraffic="true".. Better not to do
        // in a true app :)
        // In this case is ok because is a test app but remember, don't do in your REAL app
        // Only https call if you can!
        private const val DEFAULT_DOWNLOAD_LINK = "http://ipv4.download.thinkbroadband.com/10MB.zip"
    }

    private lateinit var manager:DownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        manager =
            DownloadManagerUtils.getSystemDownloadManager(
                this
            )
        val preferences:SharedPreferences = getPreferences(Context.MODE_PRIVATE)

        txt_url.editText!!.setText(preferences.getString(PREFERENCE_KEY_URL, DEFAULT_DOWNLOAD_LINK))
        layout_n_feature.visibility = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) View.VISIBLE else View.GONE
        txt_notification_description.editText?.setText(preferences.getString(PREFERENCE_KEY_DESCRIPTION,getString(R.string.default_notification_description)))
        txt_notification_title.editText?.setText(preferences.getString(PREFERENCE_KEY_TITLE,getString(R.string.default_notification_title)))
        switch_allow_mobile.isChecked = preferences.getBoolean(PREFERENCE_KEY_ALLOW_MOBILE,true)
        switch_allow_roaming.isChecked = preferences.getBoolean(PREFERENCE_KEY_ALLOW_ROAMING,true)
        switch_require_charge.isChecked = preferences.getBoolean(PREFERENCE_KEY_REQUIRE_CHARGE,false)
        switch_require_idle.isChecked = preferences.getBoolean(PREFERENCE_KEY_REQUIRE_IDLE,false)
        switch_notification_visible.isChecked = preferences.getBoolean(PREFERENCE_KEY_SHOW_NOTIFICATION,true)
        btn_download.setOnClickListener { enqueueDownload(generateDownloadRequest())}



    }

    private fun enqueueDownload(request:DownloadManager.Request){
        val id:Long = manager.enqueue(request)
        updatePreferences()
        Toast.makeText(this, String.format("New Download Enqueued with id -> %s", id), Toast.LENGTH_SHORT).show()
    }

    private fun generateDownloadRequest():DownloadManager.Request{
        val request:DownloadManager.Request = DownloadManager.Request(Uri.parse(txt_url.editText?.text.toString()))
        request.setTitle(txt_notification_title.editText?.text.toString())
        request.setDescription(txt_notification_description.editText?.text.toString())
        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS,"testFile")

        request.setAllowedOverMetered(switch_allow_mobile.isChecked)


        request.setAllowedOverRoaming(switch_allow_roaming.isChecked)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            request.setRequiresCharging(switch_require_charge.isChecked)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            request.setRequiresDeviceIdle(switch_require_idle.isChecked)
        }

        request.setNotificationVisibility(
            if(switch_notification_visible.isChecked)
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
            else
                DownloadManager.Request.VISIBILITY_HIDDEN
        )


        return request
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if(intent != null) {
            val idDownloadObject = intent.getLongExtra(DownloadObjectDialogFragment.EXTRA_DOWNLOAD_OBJECT_ID, -1)
            if (idDownloadObject >= 0)
                DownloadObjectDialogFragment.newInstance(
                    idDownloadObject
                ).show(
                    supportFragmentManager,
                    "downloadDetail"
                )
        }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_delete_files){
            val files = getDownloadedFiles()

            AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_all))
                .setMessage(getString(R.string.confirm_delete_all,files.size))
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    for(file in files){
                        file.deleteRecursively()
                    }

                }
                .setNegativeButton(getString(R.string.cancel)){ dialog, _ ->
                    dialog.dismiss()

                }
                .show()
        }
        return false
    }

    private fun updatePreferences(){
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(PREFERENCE_KEY_URL, txt_url.editText?.text.toString())
            putBoolean(PREFERENCE_KEY_SHOW_NOTIFICATION,switch_notification_visible.isChecked)
            putBoolean(PREFERENCE_KEY_REQUIRE_IDLE,switch_require_idle.isChecked)
            putBoolean(PREFERENCE_KEY_REQUIRE_CHARGE,switch_require_charge.isChecked)
            putBoolean(PREFERENCE_KEY_ALLOW_ROAMING,switch_allow_roaming.isChecked)
            putBoolean(PREFERENCE_KEY_ALLOW_MOBILE,switch_allow_mobile.isChecked)
            putString(PREFERENCE_KEY_TITLE,txt_notification_title.editText?.text.toString())
            putString(PREFERENCE_KEY_DESCRIPTION,txt_notification_description.editText?.text.toString())
            apply()
        }
    }

    private fun getDownloadedFiles():ArrayList<File>{
        val folders:Array<File> = ContextCompat.getExternalFilesDirs(this, Environment.DIRECTORY_DOWNLOADS)
        val files:ArrayList<File> = ArrayList()

        for(file:File in folders){
            val children = file.listFiles() ?: continue

            for(child in children){
                files.add(child)
            }


        }
        return files
    }


}
