package com.intellisoft.fhirstarterapp.auth

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
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

        }

    }

    private fun validateData() {
        val firstNameText = binding.firstName.text.toString()
        val lastNameText = binding.lastName.text.toString()
        val telephoneText = binding.telephone.text.toString()
        val emailText = binding.email.text.toString()
        val passwordText = binding.password.text.toString()
        val confirmPasswordText = binding.confirmPassword.text.toString()

        if (firstNameText.isEmpty()) {
            binding.apply {
                edtFirstName.error = "Please enter your first name"
                firstName.requestFocus()
            }
            return
        }

        if (lastNameText.isEmpty()) {
            binding.apply {
                edtLastName.error = "Please enter your last name"
                lastName.requestFocus()
            }
            return
        }

        if (telephoneText.isEmpty()) {
            binding.apply {
                edtTelephone.error = "Please enter your phone number"
                telephone.requestFocus()
            }
            return
        }

        if (emailText.isEmpty()) {
            binding.apply {
                edtEmail.error = "Please enter email address"
                email.requestFocus()
            }
            return
        }
        if (passwordText.isEmpty()) {
            binding.apply {
                edtPassword.error = "Please enter your password"
                password.requestFocus()
            }
            return

        }

        if (confirmPasswordText.isEmpty()) {
            binding.apply {
                edtConfirmPassword.error = "Please re-enter your password"
                confirmPassword.requestFocus()
            }
            return

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
    }
}