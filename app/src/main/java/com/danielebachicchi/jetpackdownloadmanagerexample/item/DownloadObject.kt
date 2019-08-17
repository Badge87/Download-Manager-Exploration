package com.danielebachicchi.jetpackdownloadmanagerexample.item

import android.app.DownloadManager
import android.database.Cursor
import java.util.*

class DownloadObject (val uri:String,
                      val localUri:String,
                      val bytesDownloaded:Long,
                      val bytesTotal:Long,
                      val id:Long,
                      val title:String,
                      val description:String,
                      val date:Date,
                      val mediaProviderUri:String?,
                      val mediaType:String,
                      val reason:String,
                      val status:Int) {



    companion object{
        /*
        --- DownloadManager Row Example ---
        _id : 45
        mediaprovider_uri : null
        destination : 2
        title : Download Test
        description : Downloading a file for testing Download Manager API
        uri : http://ipv4.download.thinkbroadband.com/10MB.zip
        status : 200
        hint : null
        media_type : application/zip
        total_size : 10485760
        last_modified_timestamp : 1565972803944
        bytes_so_far : 10485760
        allow_write : 0
        local_uri : content://downloads/all_downloads/45
        reason : placeholder
         --- End Row ---*/
        fun fromCursor(cursor:Cursor): DownloadObject {
            val uri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI))
            val localUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
            val bytesDownloaded = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
            val description = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION))
            val id = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID))
            val date = Date(cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP)))
            val mediaProviderUri:String? = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIAPROVIDER_URI))
            val mediaType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE))
            val reason = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            val title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
            val bytesTotal = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

            return DownloadObject(
                uri,
                localUri,
                bytesDownloaded,
                bytesTotal,
                id,
                title,
                description,
                date,
                mediaProviderUri,
                mediaType,
                reason,
                status
            )

        }
    }

    override fun toString(): String {
        return "DownloadObject(\nuri='$uri'\nlocalUri='$localUri'\nbytesDownloaded=$bytesDownloaded\nbytesTotal=$bytesTotal\nid=$id\ntitle='$title'\ndescription='$description'\ndate=$date\nmediaProviderUri=$mediaProviderUri\nmediaType='$mediaType'\nreason='$reason'\nstatus=$status\n)"
    }


}