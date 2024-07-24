package com.intellisoft.fhirstarterapp.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.intellisoft.fhirstarterapp.R
import com.intellisoft.fhirstarterapp.databinding.ActivityLoginBinding
import com.intellisoft.fhirstarterapp.databinding.ActivityRegisterBinding
import com.intellisoft.fhirstarterapp.model.PayloadData
import com.intellisoft.fhirstarterapp.model.User
import com.intellisoft.fhirstarterapp.network.RetrofitCallsAuthentication

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var retrofitCallsAuthentication = RetrofitCallsAuthentication()

    var firstName: EditText? = null
    var lastName: EditText? = null
    var telephone: EditText? = null
    var email: EditText? = null
    var password: EditText? = null
    var confirmPassword: EditText? = null
    var registerButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.apply {
            registrationButton.apply {
                setOnClickListener {
                    validateData()
                }
            }

            loginLink.setOnClickListener{
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

        if (passwordText != confirmPasswordText) {
            Toast.makeText(this, "Password Not matching", Toast.LENGTH_SHORT)
                .show()
        }

        val data = User(
            User = PayloadData(
                username = emailText, name = "$firstNameText $lastNameText", telephone = telephoneText, email = emailText,
                password = passwordText, confirm_password = confirmPasswordText
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
            registerButton?.setEnabled(
                firstNameInput.isNotEmpty() && lastNameInput.isNotEmpty() && telephoneInput.isNotEmpty()
                        && emailInput.isNotEmpty() && passwordInput.isNotEmpty() &&
                        confirmPasswordInput.isNotEmpty()
            )
        }

        override fun afterTextChanged(s: Editable) {}
    }
}