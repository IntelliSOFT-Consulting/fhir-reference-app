package com.intellisoft.fhirstarterapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.fhir.search.Order
import com.google.android.fhir.search.search
import com.google.android.fhir.sync.CurrentSyncJobStatus
import com.google.android.fhir.sync.Sync
import com.google.android.fhir.sync.SyncJobStatus
import com.intellisoft.fhirstarterapp.fhir.AppFhirSyncWorker
import com.intellisoft.fhirstarterapp.fhir.FhirApplication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Patient

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val _pollState = MutableSharedFlow<CurrentSyncJobStatus>()

    val pollState: Flow<CurrentSyncJobStatus>
        get() = _pollState

    val liveSearchedPatients = MutableLiveData<List<Patient>>()

    init {
        updatePatientList { getSearchResults() }
    }

    fun triggerOneTimeSync() {
        viewModelScope.launch {
            Sync.oneTimeSync<AppFhirSyncWorker>(getApplication())
                .shareIn(this, SharingStarted.Eagerly, 10)
                .collect { _pollState.emit(it) }
        }
    }

    /*
    Fetches patients stored locally based on the city they are in, and then updates the city field for
    each patient. Once that is complete, trigger a new sync so the changes can be uploaded.
     */
    fun triggerUpdate() {
        // Add code to trigger update
    }

    fun searchPatientsByName(nameQuery: String) {
        // Add code to use fhirEngine to search for patients
    }

    /**
     * [updatePatientList] calls the search and count lambda and updates the live data values
     * accordingly. It is initially called when this [ViewModel] is created. Later its called by the
     * client every time search query changes or data-sync is completed.
     */
    private fun updatePatientList(
        search: suspend () -> List<Patient>,
    ) {
        viewModelScope.launch { liveSearchedPatients.value = search() }
    }

    private suspend fun getSearchResults(): List<Patient> {
        val patients: MutableList<Patient> = mutableListOf()
//        FhirApplication.fhirEngine(this.getApplication())
//            .search<Patient> { sort(Patient.GIVEN, Order.ASCENDING) }
//            .let { patients.addAll(it.map { it.resource }) }
        return patients
    }

}