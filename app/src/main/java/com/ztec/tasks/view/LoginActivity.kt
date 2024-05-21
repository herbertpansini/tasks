package com.ztec.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.ztec.tasks.R
import com.ztec.tasks.databinding.ActivityLoginBinding
import com.ztec.tasks.service.constants.TaskConstants
import com.ztec.tasks.service.repository.SecurityPreferences
import com.ztec.tasks.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private val securityPreferences by lazy { SecurityPreferences(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener(this)

        viewModel.verifyAuthentication()

        observe()
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_login) {
            handleLogin()
        }
    }

    private fun observe() {
        viewModel.login.observe(this) {
            if (it.status()) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(applicationContext, it.message(), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loggedUser.observe(this) {
            if (it) {
                this.biometricAuthentication()
            }
        }
    }

    private fun biometricAuthentication() {
        val executor = ContextCompat.getMainExecutor(this)
        val bio = BiometricPrompt(this, executor, object: BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)

                viewModel.doLogin(securityPreferences.get(TaskConstants.USER.EMAIL),
                                  securityPreferences.get(TaskConstants.USER.PASSWORD),
                                  securityPreferences.get(TaskConstants.USER.DEVICE_TOKEN))
            }
        })
        val info = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Title")
                    .setSubtitle("Subtitle")
                    .setDescription("Description")
                    .setNegativeButtonText("Cancelar")
                    .build()
        bio.authenticate(info)
    }

    private fun handleLogin() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()
        val deviceToken = securityPreferences.get(TaskConstants.USER.DEVICE_TOKEN)

        viewModel.doLogin(email, password, deviceToken)
    }
}