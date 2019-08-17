package com.danielebachicchi.jetpackdownloadmanagerexample.view

import android.app.Dialog
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.danielebachicchi.jetpackdownloadmanagerexample.BuildConfig
import com.danielebachicchi.jetpackdownloadmanagerexample.R
import com.danielebachicchi.jetpackdownloadmanagerexample.utils.DownloadManagerUtils
import com.danielebachicchi.jetpackdownloadmanagerexample.utils.FileUtils
import kotlinx.android.synthetic.main.dialog_download_detail.view.*
import java.io.File


class DownloadObjectDialogFragment: DialogFragment() {



    companion object{
        const val EXTRA_DOWNLOAD_OBJECT_ID = "downloadID"
        fun newInstance(downloadObjectID:Long): DownloadObjectDialogFragment {
            val result = DownloadObjectDialogFragment()

            result.arguments = Bundle().apply {
                putLong(EXTRA_DOWNLOAD_OBJECT_ID,downloadObjectID)
            }

            return result

        }
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {



        return activity?.let {
            val inflater = requireActivity().layoutInflater
            // Use the Builder class for convenient dialog construction
            val downloadObjectID = arguments!!.getLong(EXTRA_DOWNLOAD_OBJECT_ID)
            val downloadObject =
                DownloadManagerUtils.queryDownloadManagerObject(
                    DownloadManagerUtils.getSystemDownloadManager(
                        context!!
                    ),
                    downloadObjectID
                )

            val builder = AlertDialog.Builder(it)
            val view:View = inflater.inflate(R.layout.dialog_download_detail, null)
            builder.setView(view)
                .setCancelable(false)
                .setNeutralButton(getString(R.string.delete)) { dialog, _ ->
                    if(context != null) {
                        val downloadManager:DownloadManager =
                            DownloadManagerUtils.getSystemDownloadManager(
                                context!!
                            )
                        downloadManager.remove(arguments!!.getLong(EXTRA_DOWNLOAD_OBJECT_ID))
                    }
                    dialog.dismiss()
                }
                .setPositiveButton(getString(R.string.open_file)){ _, _ ->
                    val intent = Intent()
                    var uri = Uri.parse(downloadObject!!.localUri)
                    intent.action = Intent.ACTION_VIEW
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                    if (ContentResolver.SCHEME_FILE == uri.scheme) {
                        // FileUri - Convert it to contentUri.
                        val file = File(uri.path!!)
                        uri = FileProvider.getUriForFile(context!!,
                            BuildConfig.FILE_PROVIDER_AUTHORITY, file)
                    }

                    context!!.grantUriPermission(context!!.packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.setDataAndType(uri, downloadObject.mediaType)

                    try {
                        context!!.startActivity(intent)
                    }catch(e:ActivityNotFoundException){
                        Toast.makeText(context!!,context!!.getString(R.string.err_no_activity_found), Toast.LENGTH_SHORT).show()
                    }


                }

            view.txt_name.text = downloadObject!!.uri
            view.txt_size.text = FileUtils.formatFileSize(
                downloadObject.bytesTotal,
                true
            )
            // Create the AlertDialog object and return it
            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")

    }
}