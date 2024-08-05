package com.intellisoft.fhirstarterapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intellisoft.fhirstarterapp.model.UniversalMap

class FhirViewModel : ViewModel() {

    private val _designations = MutableLiveData<MutableList<UniversalMap>>().apply {
        value = mutableListOf() // Initial value is a false
    }
    val designations: LiveData<MutableList<UniversalMap>> = _designations

        fun getDesignations(query: ArrayList<UniversalMap>) {
        Log.e("TAG", "Post data $query")
        if (query.isNotEmpty()) {
            _designations.value = query
            Log.e("TAG", "Post data 1 ${_designations.value}")
            Log.e("TAG", "Post data 2 ${designations.value}")
        }
    }

    private val _counties = MutableLiveData<MutableList<UniversalMap>>().apply {
        value = mutableListOf() // Initial value is a false
    }

    val counties: LiveData<MutableList<UniversalMap>> = _counties

    fun getCounties(query: ArrayList<UniversalMap>) {
        if (query.isNotEmpty()) {
            _counties.value = query
            Log.e("TAG", "Post data 1 ${_designations.value}")
            Log.e("TAG", "Post data 2 ${designations.value}")
        }
    }

}