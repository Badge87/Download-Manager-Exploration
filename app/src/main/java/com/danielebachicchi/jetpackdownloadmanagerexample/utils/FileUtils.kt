package com.danielebachicchi.jetpackdownloadmanagerexample.utils

import java.util.*

class FileUtils {
    companion object{
         fun formatFileSize(number: Long, shorter: Boolean): String {
            var suffix = ""
            var result = number.toFloat()
            if (result > 900) {
                result /= 1024
                suffix = "KB"
            }
            if (result > 900) {
                result /= 1024
                suffix = "MB"
            }
            if (result > 900) {
                result /= 1024
                suffix = "GB"
            }
            if (result > 900) {
                result /= 1024
                suffix = "TB"
            }
            if (result > 900) {
                result /= 1024
                suffix = "PB"
            }
            val value: String
             value = when {
                 result < 1 -> String.format(Locale.getDefault(), "%.2f", result)
                 result < 10 -> if (shorter) {
                     String.format(Locale.getDefault(), "%.1f", result)
                 } else {
                     String.format(Locale.getDefault(), "%.2f", result)
                 }
                 result < 100 -> if (shorter) {
                     String.format(Locale.getDefault(), "%.0f", result)
                 } else {
                     String.format(Locale.getDefault(), "%.2f", result)
                 }
                 else -> String.format(Locale.getDefault(), "%.0f", result)
             }
            return "$value $suffix"
        }
    }
}