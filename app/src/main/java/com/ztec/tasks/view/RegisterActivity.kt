package com.ztec.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ztec.tasks.R
import com.ztec.tasks.databinding.ActivityRegisterBinding
import com.ztec.tasks.service.constants.TaskConstants
import com.ztec.tasks.service.model.UserModel
import com.ztec.tasks.service.repository.SecurityPreferences
import com.ztec.tasks.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding
    private val securityPreferences by lazy { SecurityPreferences(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observe()
    }

    private fun observe() {
        viewModel.user.observe(this) {
            if (it.status()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, it.message(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSave() {
        val user = UserModel().apply {
            this.name = binding.editName.text.toString()
            this.email = binding.editEmail.text.toString()
            this.password = binding.editPassword.text.toString()
            this.passwordConfirmation = binding.editPasswordConfirmation.text.toString()
            this.role = "ROLE_USER"
            this.deviceToken = ""
        }

        viewModel.register(user)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.register, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                handleSave()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}