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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        // Layout
        setContentView(binding.root)

        // Eventos
        binding.buttonLogin.setOnClickListener(this)
        binding.textRegister.setOnClickListener(this)

        viewModel.verifyAuthentication()

        // Observadores
        observe()
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_login) {
            handleLogin()
        } else if (v.id == R.id.text_register) {
            startActivity(Intent(this, RegisterActivity::class.java))
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

                val securityPreferences = SecurityPreferences(applicationContext)
                viewModel.doLogin(securityPreferences.get(TaskConstants.USER.EMAIL),
                                  securityPreferences.get(TaskConstants.USER.PASSWORD),
                                  securityPreferences.get(TaskConstants.USER.DEVICE))
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
        //TODO: () -> Recuperar instancia do dispositivo toda vez que fizer o login
        val device = "instancia_do_dispositivo"

        viewModel.doLogin(email, password, device)
    }
}