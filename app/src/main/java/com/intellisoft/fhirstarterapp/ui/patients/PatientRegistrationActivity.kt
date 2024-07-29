package com.intellisoft.fhirstarterapp.ui.patients

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.google.android.fhir.datacapture.QuestionnaireFragment
import com.intellisoft.fhirstarterapp.R
import com.intellisoft.fhirstarterapp.databinding.ActivityPatientRegistrationBinding

class PatientRegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientRegistrationBinding
    var questionnaireJsonString: String? = null
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

        // Step 2: Configure a QuestionnaireFragment
//        val questionnaireJsonString = getStringFromAssets("questionnaire.json")

//        val questionnaireParams = bundleOf(
//            QuestionnaireFragment.EXTRA_QUESTIONNAIRE_JSON_STRING to questionnaireJsonString
//        )
//        // Step 3: Add the QuestionnaireFragment to the FragmentContainerView
//        if (savedInstanceState == null) {
//            supportFragmentManager.commit {
//                setReorderingAllowed(true)
//                add<QuestionnaireFragment>(R.id.fragment_container_view, args = questionnaireParams)
//            }
//        }
    }

    private fun getStringFromAssets(fileName: String): String {
        return assets.open(fileName).bufferedReader().use { it.readText() }
    }
}