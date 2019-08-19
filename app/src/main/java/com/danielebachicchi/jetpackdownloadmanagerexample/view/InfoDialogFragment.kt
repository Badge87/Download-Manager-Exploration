package com.danielebachicchi.jetpackdownloadmanagerexample.view

import android.app.Dialog
import android.content.Intent
import android.net.Uri

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.danielebachicchi.jetpackdownloadmanagerexample.BuildConfig
import com.danielebachicchi.jetpackdownloadmanagerexample.R
import kotlinx.android.synthetic.main.dialog_info.view.*



class InfoDialogFragment: DialogFragment() {



    companion object{
        fun newInstance(): InfoDialogFragment {

            return InfoDialogFragment()

        }
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {



        return activity?.let {
            val inflater = requireActivity().layoutInflater
            // Use the Builder class for convenient dialog construction

            val builder = AlertDialog.Builder(it)
            val view:View = inflater.inflate(R.layout.dialog_info, null)
            builder.setView(view)
                .setPositiveButton(""){ _, _ ->



                }
            val version = "v " + BuildConfig.VERSION_NAME

            view.txt_version.text = version


            view.btn_facebook.setOnClickListener { openLink(getString(R.string.link_facebook)) }
            view.btn_github.setOnClickListener { openLink(getString(R.string.link_github)) }
            view.btn_linkedin.setOnClickListener { openLink(getString(R.string.link_linkedin)) }
            view.btn_twitter.setOnClickListener { openLink(getString(R.string.link_twitter)) }

            // Create the AlertDialog object and return it
            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")

    }


    fun openLink(link:String){
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }
}