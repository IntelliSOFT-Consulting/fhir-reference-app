package com.intellisoft.fhirstarterapp.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.intellisoft.fhirstarterapp.MainActivity
import com.intellisoft.fhirstarterapp.R
import com.intellisoft.fhirstarterapp.databinding.ActivityLoginBinding
import com.intellisoft.fhirstarterapp.databinding.ActivityMainBinding
import com.intellisoft.fhirstarterapp.model.PayloadData
import com.intellisoft.fhirstarterapp.model.User
import com.intellisoft.fhirstarterapp.network.RetrofitCallsAuthentication
import com.intellisoft.fhirstarterapp.utils.LocalData

class LoginActivity : AppCompatActivity() {
    private var retrofitCallsAuthentication = RetrofitCallsAuthentication()
    private lateinit var binding: ActivityLoginBinding

    var email: EditText? = null
    var password: EditText? = null
    var loginButton: Button? = null

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
                    validateData()
                }

            }

            signupLink.setOnClickListener{
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        loginButton = findViewById(R.id.login_button)

        email?.addTextChangedListener(textWatcher)
        password?.addTextChangedListener(textWatcher)
    }

    private fun validateData() {
        val emailText = binding.email.text.toString()
        val passwordText = binding.password.text.toString()

        val data = User(
            User = PayloadData(
                username = emailText, password = passwordText
            )
        )
        retrofitCallsAuthentication.loginUser(
            this@LoginActivity, data
        )

    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            // Get the content of both the edit texts
            val emailInput = email!!.text.toString()
            val passwordInput = password!!.text.toString()
            // Check whether both the fields are empty or not
            loginButton?.setEnabled(emailInput.isNotEmpty() && passwordInput.isNotEmpty())
        }

        override fun afterTextChanged(s: Editable) {}
    }
}