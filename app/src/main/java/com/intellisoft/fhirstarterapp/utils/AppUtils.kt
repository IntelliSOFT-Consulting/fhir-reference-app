package com.intellisoft.fhirstarterapp.utils

import android.content.Context

class AppUtils {

    fun getStringFromAssets(fileName: String, context: Context): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
}