package com.intellisoft.fhirstarterapp.fhir.extensions

import android.content.Context

fun Context.readFileFromAssets(fileName: String): String =
    assets.open(fileName).bufferedReader().use { it.readText() }