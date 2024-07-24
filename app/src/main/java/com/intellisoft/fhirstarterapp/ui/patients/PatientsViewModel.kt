package com.intellisoft.fhirstarterapp.ui.patients

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.search.Order
import com.google.android.fhir.search.StringFilterModifier
import com.google.android.fhir.search.count
import com.google.android.fhir.search.search
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Patient

class PatientViewModel(application: Application, private val fhirEngine: FhirEngine) :
    AndroidViewModel(application) {

    val liveSearchedPatients = MutableLiveData<List<PatientItem>>()
    val patientCount = MutableLiveData<Long>()

    init {

        updatePatientListAndPatientCount({ getSearchResults() }, { count() })
    }

    fun searchPatientsByName(nameQuery: String) {
        updatePatientListAndPatientCount({ getSearchResults(nameQuery) }, { count(nameQuery) })
    }

    /**
     * [updatePatientListAndPatientCount] calls the search and count lambda and updates the live data
     * values accordingly. It is initially called when this [ViewModel] is created. Later its called
     * by the client every time search query changes or data-sync is completed.
     */
    private fun updatePatientListAndPatientCount(
        search: suspend () -> List<PatientItem>,
        count: suspend () -> Long,
    ) {
        viewModelScope.launch {
            liveSearchedPatients.value = search()
            patientCount.value = count()
        }
    }

    /**
     * Returns count of all the [Patient] who match the filter criteria unlike [getSearchResults]
     * which only returns a fixed range.
     */
    private suspend fun count(nameQuery: String = ""): Long {
        val count = fhirEngine.count<Patient> {
            if (nameQuery.isNotEmpty()) {
                filter(
                    Patient.NAME,
                    {
                        modifier = StringFilterModifier.CONTAINS
                        value = nameQuery
                    },
                )
            }
        }
        return count
    }

    private suspend fun getSearchResults(nameQuery: String = ""): List<PatientItem> {
        val patients: MutableList<PatientItem> = mutableListOf()
        fhirEngine
            .search<Patient> {
                if (nameQuery.isNotEmpty()) {
                    filter(
                        Patient.NAME,
                        {
                            modifier = StringFilterModifier.CONTAINS
                            value = nameQuery
                        },
                    )
                }
                sort(Patient.GIVEN, Order.ASCENDING)
                count = 1000
                from = 0
            }
            .mapIndexed { index, fhirPatient -> fhirPatient.toPatientItem(index + 1) }
            .let { patients.addAll(it) }
        return patients
    }

    /** The Patient's details for display purposes. */
    data class PatientItem(
        val id: String,
        val resourceId: String,
        val name: String,
        val gender: String,
        val dob: LocalDate? = null,
        val phone: String,
        val city: String,
        val country: String,
        val isActive: Boolean
    ) {
        override fun toString(): String = name
    }

    class PatientViewModelFactory(
        private val application: Application,
        private val fhirEngine: FhirEngine,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PatientViewModel::class.java)) {
                return PatientViewModel(application, fhirEngine) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

internal fun Patient.toPatientItem(position: Int): PatientViewModel.PatientItem {
    // Show nothing if no values available for gender and date of birth.
    val patientId = if (hasIdElement()) idElement.idPart else ""
    val name = if (hasName()) name[0].nameAsSingleString else ""
    val gender = if (hasGenderElement()) genderElement.valueAsString else ""
    val dob =
        if (hasBirthDateElement()) {
            try {
                LocalDate.parse(birthDateElement.valueAsString, DateTimeFormatter.ISO_DATE)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    val phone = if (hasTelecom()) telecom[0].value else ""
    val city = if (hasAddress()) address[0].city else ""
    val country = if (hasAddress()) address[0].country else ""
    val isActive = active

    return PatientViewModel.PatientItem(
        id = position.toString(),
        resourceId = patientId,
        name = name,
        gender = gender ?: "",
        dob = dob,
        phone = phone ?: "",
        city = city ?: "",
        country = country ?: "",
        isActive = isActive
    )
}