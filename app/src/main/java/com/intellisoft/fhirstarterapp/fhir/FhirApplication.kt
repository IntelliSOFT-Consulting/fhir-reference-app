package com.intellisoft.fhirstarterapp.fhir

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.android.fhir.DatabaseErrorStrategy
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.FhirEngineConfiguration
import com.google.android.fhir.FhirEngineProvider
import com.google.android.fhir.ServerConfiguration
import com.google.android.fhir.sync.Sync
import com.google.android.fhir.sync.remote.HttpLogger


class FhirApplication : Application() {
    // Only initiate the FhirEngine when used for the first time, not when the app is created.
    private val fhirEngine: FhirEngine by lazy { constructFhirEngine() }

    private val dataStore by lazy { DemoDataStore(this) }

    override fun onCreate() {
        super.onCreate()

        FhirEngineProvider.init(
            FhirEngineConfiguration(
                enableEncryptionIfSupported = false,
                DatabaseErrorStrategy.RECREATE_AT_OPEN,
                ServerConfiguration(
                    "https://hapi.fhir.org/baseR4/",
                    httpLogger =
                    HttpLogger(
                        HttpLogger.Configuration(
                          HttpLogger.Level.BASIC
                        )
                    ) { Log.e("TAG","App-HttpLog $it") }
                )
            )
        )
        Sync.oneTimeSync<FhirSyncWorker>(this)


    }

    private fun constructFhirEngine(): FhirEngine {
        return FhirEngineProvider.getInstance(this)
    }

    companion object {
        fun fhirEngine(context: Context) = (context.applicationContext as FhirApplication).fhirEngine

        fun dataStore(context: Context) = (context.applicationContext as FhirApplication).dataStore
    }


}