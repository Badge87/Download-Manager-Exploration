package com.danielebachicchi.jetpackdownloadmanagerexample.utils

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import com.danielebachicchi.jetpackdownloadmanagerexample.item.DownloadObject

class DownloadManagerUtils {
    companion object{

        fun getSystemDownloadManager(context:Context):DownloadManager{
            return context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        }

        fun queryDownloadManagerObject(downloadManager:DownloadManager, id:Long): DownloadObject?{

           val cursor:Cursor = downloadManager.query(DownloadManager.Query().setFilterById(id))

            var result: DownloadObject? = null
            if (cursor.moveToFirst())
                result = DownloadObject.fromCursor(cursor)


           return result


        }
    }
}