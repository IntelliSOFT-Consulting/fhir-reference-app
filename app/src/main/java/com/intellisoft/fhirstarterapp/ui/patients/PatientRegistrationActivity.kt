package com.intellisoft.fhirstarterapp.ui.patients

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.google.android.fhir.datacapture.QuestionnaireFragment
import com.intellisoft.fhirstarterapp.R
import com.intellisoft.fhirstarterapp.databinding.ActivityPatientRegistrationBinding

import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.QuestionnaireResponse

class PatientRegistrationActivity : AppCompatActivity() {


    private lateinit var binding: ActivityPatientRegistrationBinding
    private val viewModel: AddPatientViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPatientRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState == null) {
            addQuestionnaireFragment()
        }

        observePatientSaveAction()
        /** Use the provided cancel|submit buttons from the sdc library */
        supportFragmentManager.setFragmentResultListener(
            QuestionnaireFragment.SUBMIT_REQUEST_KEY,
            this,
        ) { _, _ ->
            onSubmitAction()
        }
        supportFragmentManager.setFragmentResultListener(
            QuestionnaireFragment.CANCEL_REQUEST_KEY,
            this,
        ) { _, _ ->
            this@PatientRegistrationActivity.finish()
        }

    }


    private fun addQuestionnaireFragment() {
        supportFragmentManager.commit {
            add(
                R.id.fragment_container_view,
                QuestionnaireFragment.builder()
                    .setQuestionnaire(viewModel.questionnaireJson)
                    .setShowCancelButton(true)
//                    .setSubmitButtonText(
//                        getString(com.google.android.fhir.datacapture.R.string.submit_questionnaire),
//                    )


                    .build(),
                QUESTIONNAIRE_FRAGMENT_TAG,
            )
        }
    }

    private fun onSubmitAction() {
        lifecycleScope.launch {
            val questionnaireFragment =
                supportFragmentManager.findFragmentByTag(QUESTIONNAIRE_FRAGMENT_TAG) as QuestionnaireFragment
            savePatient(questionnaireFragment.getQuestionnaireResponse())
        }
    }

    private fun savePatient(questionnaireResponse: QuestionnaireResponse) {
        viewModel.savePatient(questionnaireResponse)
    }

    private fun observePatientSaveAction() {


        viewModel.isPatientSaved.observe(this) {
            if (!it) {
                Toast.makeText(
                    this@PatientRegistrationActivity,
                    "Inputs are missing.",
                    Toast.LENGTH_SHORT
                ).show()
                return@observe
            }
            Toast.makeText(
                this@PatientRegistrationActivity,
                "Patient is saved.",
                Toast.LENGTH_SHORT
            ).show()
            this@PatientRegistrationActivity.finish()

        }
    }

    companion object {
        const val QUESTIONNAIRE_FILE_PATH_KEY = "questionnaire-file-path-key"
        const val QUESTIONNAIRE_FRAGMENT_TAG = "questionnaire-fragment-tag"
    }


}