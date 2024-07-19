package com.intellisoft.fhirstarterapp.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.intellisoft.fhirstarterapp.MainActivity
import com.intellisoft.fhirstarterapp.R
import com.intellisoft.fhirstarterapp.databinding.ActivityLoginBinding
import com.intellisoft.fhirstarterapp.databinding.ActivityMainBinding
import com.intellisoft.fhirstarterapp.model.LoginData
import com.intellisoft.fhirstarterapp.model.User
import com.intellisoft.fhirstarterapp.network.RetrofitCallsAuthentication
import com.intellisoft.fhirstarterapp.utils.LocalData

class LoginActivity : AppCompatActivity() {
    private var retrofitCallsAuthentication = RetrofitCallsAuthentication()
    private lateinit var binding: ActivityLoginBinding

    override fun onStart() {
        super.onStart()
        try {
            val isLoggedIn = LocalData().getSharedPref("isLoggedIn", this)
            if (isLoggedIn != null) {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)

                this@LoginActivity.finish()
            }

        } catch (e: Exception) {
            e.printStackTrace()

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            loginButton.apply {
                setOnClickListener {
                    validataData()
                }

            }

            signupLink.apply {

            }
        }
    }

    private fun validataData() {
        val emailText = binding.email.text.toString()
        val passwordText = binding.password.text.toString()

        if (emailText.isEmpty()) {
            binding.apply {
                teiEmail.error = "Please enter email address"
                email.requestFocus()
            }
            return
        }
        if (passwordText.isEmpty()) {
            binding.apply {
                teiPassword.error = "Please enter password"
                password.requestFocus()
            }
            return
        }

        val data = User(
            User = LoginData(
                username = emailText, password = passwordText
            )
        )
        retrofitCallsAuthentication.loginUser(
            this, data
        )

    }
}