package com.intellisoft.fhirstarterapp.ui.patients

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.fhir.FhirEngine
import com.intellisoft.fhirstarterapp.databinding.FragmentPatientsBinding
import com.intellisoft.fhirstarterapp.fhir.FhirApplication
import com.intellisoft.fhirstarterapp.ui.patients.PatientViewModel.PatientItem
import com.intellisoft.fhirstarterapp.viewmodels.MainActivityViewModel
import kotlinx.coroutines.launch

class PatientsFragment : Fragment() {

    private var _binding: FragmentPatientsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var fhirEngine: FhirEngine
    private lateinit var patientListViewModel: PatientViewModel

    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    val patients: MutableList<String> = mutableListOf()
    lateinit var adapter1: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPatientsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        adapter1 = ArrayAdapter(requireContext(), R.layout.simple_list_item_1, patients)
        binding.listView.apply {
            adapter = adapter1

        }
        return root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        fhirEngine = FhirApplication.fhirEngine(requireContext())
        patientListViewModel =
            ViewModelProvider(
                this,
                PatientViewModel.PatientViewModelFactory(requireActivity().application, fhirEngine),
            )
                .get(PatientViewModel::class.java)


        patientListViewModel.liveSearchedPatients.observe(viewLifecycleOwner) { k ->
            val uniquePatients = mutableSetOf<String>() // Using a set to ensure uniqueness
            k.forEach {
                uniquePatients.add(it.name)
            }
            patients.clear()
            patients.addAll(uniquePatients)

            adapter1.notifyDataSetChanged()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}