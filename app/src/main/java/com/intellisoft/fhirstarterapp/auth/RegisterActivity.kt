package com.intellisoft.fhirstarterapp.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.intellisoft.fhirstarterapp.R
import com.intellisoft.fhirstarterapp.databinding.ActivityRegisterBinding
import com.intellisoft.fhirstarterapp.model.PayloadData
import com.intellisoft.fhirstarterapp.model.User
import com.intellisoft.fhirstarterapp.network.RetrofitCallsAuthentication
import com.intellisoft.fhirstarterapp.viewModel.FhirViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var retrofitCallsAuthentication = RetrofitCallsAuthentication()
    private lateinit var viewModel: FhirViewModel
    var firstName: EditText? = null
    var lastName: EditText? = null
    var telephone: EditText? = null
    var email: EditText? = null
    var password: EditText? = null
    var confirmPassword: EditText? = null
    var registerButton: Button? = null
    private val map = HashMap<String, String>()

    //private val viewModel: CountyViewModel by ViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[FhirViewModel::class.java]
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val adapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, retrieveCountyData())
        binding.apply {
            actCounty.setAdapter(adapter1)
        }

        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, retrieveDesignationData())
        binding.actDesignations.apply {
            setAdapter(adapter2)
        }

        binding.apply {
            registrationButton.apply {
                setOnClickListener {
                    validateData()
                }
            }

            loginLink.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }

        firstName = findViewById(R.id.firstName)
        lastName = findViewById(R.id.lastName)
        telephone = findViewById(R.id.telephone)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirmPassword)
        registerButton = findViewById(R.id.registration_button)

        firstName?.addTextChangedListener(textWatcher)
        lastName?.addTextChangedListener(textWatcher)
        telephone?.addTextChangedListener(textWatcher)
        email?.addTextChangedListener(textWatcher)
        password?.addTextChangedListener(textWatcher)
        confirmPassword?.addTextChangedListener(textWatcher)
    }

    private fun retrieveDesignationData(): List<String> {
        val data = ArrayList<String>()
        retrofitCallsAuthentication.loadDesignations(this, viewModel)

        viewModel.designations.observe(this@RegisterActivity) {
            if (it.isNotEmpty()) {
                Log.e("Tag", "Designation List reg $it")
                it.forEach{
                    d -> data.add(d.value)
                    map[d.value] = d.code
                }

            } else {
                Log.e("Tag", "Designation List reg empty list")
            }
        }
        return data
    }

    private fun retrieveCountyData(): List<String> {
        val data = ArrayList<String>()
        retrofitCallsAuthentication.loadCounties(this, viewModel)

        viewModel.counties.observe(this@RegisterActivity ) {
            if (it.isNotEmpty()){
                Log.e("Tag", "CountyList $it")
                it.forEach{
                    c -> data.add(c.value)
                    map[c.value] = c.code
                }
            }
            Log.e("Tag", "County List $it")
        }
        return data
    }


    val ERR_LEN = "Password must have at least eight characters!"
    val ERR_WHITESPACE = "Password must not contain whitespace!"
    val ERR_DIGIT = "Password must contain at least one digit!"
    val ERR_UPPER = "Password must have at least one uppercase letter!"
    val ERR_SPECIAL = "Password must have at least one special character, such as: _%-=+#@"

    fun validatePassword(pwd: String) = runCatching {
        require(pwd.length >= 8) { ERR_LEN }
        require(pwd.none { it.isWhitespace() }) { ERR_WHITESPACE }
        require(pwd.any { it.isDigit() }) { ERR_DIGIT }
        require(pwd.any { it.isUpperCase() }) { ERR_UPPER }
        require(pwd.any { !it.isLetterOrDigit() }) { ERR_SPECIAL }
    }

    private fun validateData() {
        val firstNameText = binding.firstName.text.toString()
        val lastNameText = binding.lastName.text.toString()
        val telephoneText = binding.telephone.text.toString()
        val emailText = binding.email.text.toString()
        val passwordText = binding.password.text.toString()
        val confirmPasswordText = binding.confirmPassword.text.toString()

        val des = binding.actDesignations.text.toString()
        if (des.isEmpty()){
            binding.teiDesignations.error = "Please select your designation"
            binding.actDesignations.requestFocus()
            return
        }
        val designationId = map[des]

        val cou = binding.actCounty.text.toString()
        if (cou.isEmpty()){
            binding.teiCounty.error = "Please select your county"
            binding.actCounty.requestFocus()
            return
        }
        val countyId = map[cou]


        if (passwordText != confirmPasswordText) {
            Toast.makeText(this, "Passwords not matching", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val data = User(
            User = PayloadData(
                username = emailText,
                name = "$firstNameText $lastNameText",
                telephone = telephoneText,
                email = emailText,
                password = passwordText,
                designationId = designationId.toString(),
                countyId = countyId.toString(),
                confirm_password = confirmPasswordText
            )
        )
        retrofitCallsAuthentication.registerUser(
            this, data
        )

//        validatePassword(tooShort).apply {
//            assertTrue { isFailure }
//            assertEquals(ERR_LEN, exceptionOrNull()?.message)
//        }
//
//        validatePassword(noDigit).apply {
//            assertTrue { isFailure }
//            assertEquals(ERR_DIGIT, exceptionOrNull()?.message)
//        }
//
//        validatePassword(withSpace).apply {
//            assertTrue { isFailure }
//            assertEquals(ERR_WHITESPACE, exceptionOrNull()?.message)
//        }
//
//        validatePassword(noUpper).apply {
//            assertTrue { isFailure }
//            assertEquals(ERR_UPPER, exceptionOrNull()?.message)
//        }
//
//        validatePassword(noSpecial).apply {
//            assertTrue { isFailure }
//            assertEquals(ERR_SPECIAL, exceptionOrNull()?.message)
//        }
//
//        validatePassword(okPwd).apply {
//            assertTrue { isSuccess }
//        }
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            // Get the content of all the edit texts
            val firstNameInput = firstName!!.text.toString()
            val lastNameInput = lastName!!.text.toString()
            val telephoneInput = telephone!!.text.toString()
            val emailInput = email!!.text.toString()
            val passwordInput = password!!.text.toString()
            val confirmPasswordInput = confirmPassword!!.text.toString()
            // Check whether all the fields are empty or not
            registerButton?.isEnabled = (firstNameInput.isNotEmpty() && lastNameInput.isNotEmpty() && telephoneInput.isNotEmpty()
                    && emailInput.isNotEmpty() && passwordInput.isNotEmpty() &&
                    confirmPasswordInput.isNotEmpty())
        }

        override fun afterTextChanged(s: Editable) {}
    }
}