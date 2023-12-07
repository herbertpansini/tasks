package com.ztec.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ztec.tasks.service.constants.TaskConstants
import com.ztec.tasks.service.helper.BiometricHelper
import com.ztec.tasks.service.listener.APIListener
import com.ztec.tasks.service.model.PersonModel
import com.ztec.tasks.service.model.ValidationModel
import com.ztec.tasks.service.repository.PersonRepository
import com.ztec.tasks.service.repository.SecurityPreferences
import com.ztec.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _login = MutableLiveData<ValidationModel>()
    val login: LiveData<ValidationModel> = _login

    private val _loggedUser = MutableLiveData<Boolean>()
    val loggedUser: LiveData<Boolean> = _loggedUser

    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String, device: String) {
        personRepository.login(email, password, device, object: APIListener<PersonModel> {
            override fun onSuccess(result: PersonModel) {
                securityPreferences.store(TaskConstants.USER.ID, result.id)
                securityPreferences.store(TaskConstants.USER.NAME, result.name)
                securityPreferences.store(TaskConstants.USER.EMAIL, email)
                securityPreferences.store(TaskConstants.USER.PASSWORD, password)
                securityPreferences.store(TaskConstants.USER.DEVICE, device)
                securityPreferences.store(TaskConstants.USER.ROLE, result.role)

                securityPreferences.store(TaskConstants.HEADER.TOKEN_VALUE, result.token)

                RetrofitClient.addHeaders(result.token)

                _login.value = ValidationModel()
            }

            override fun onFailure(message: String) {
                _login.value = ValidationModel(message)
            }
        })
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyAuthentication() {
        _loggedUser.value = (securityPreferences.get(TaskConstants.HEADER.TOKEN_VALUE) != "" &&
                             BiometricHelper.isBiometricAvailable(getApplication()))
    }
}