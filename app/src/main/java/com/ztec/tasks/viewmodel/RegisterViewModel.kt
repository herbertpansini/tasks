package com.ztec.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ztec.tasks.service.constants.TaskConstants
import com.ztec.tasks.service.listener.APIListener
import com.ztec.tasks.service.model.UserModel
import com.ztec.tasks.service.model.ValidationModel
import com.ztec.tasks.service.repository.SecurityPreferences
import com.ztec.tasks.service.repository.UserRepository

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _user = MutableLiveData<ValidationModel>()
    val user: LiveData<ValidationModel> = _user

    fun register(user: UserModel) {
        userRepository.register(user, object: APIListener<UserModel>{
            override fun onSuccess(result: UserModel) {
                securityPreferences.store(TaskConstants.USER.ID, result.id.toString())
                securityPreferences.store(TaskConstants.USER.NAME, result.name)
                securityPreferences.store(TaskConstants.USER.EMAIL, result.email)
                securityPreferences.store(TaskConstants.USER.PASSWORD, result.password)
                securityPreferences.store(TaskConstants.USER.ROLE, result.role)

//                securityPreferences.store(TaskConstants.HEADER.TOKEN_VALUE, result.token)
//
//                RetrofitClient.addHeaders(result.token)

                _user.value = ValidationModel()
            }
            override fun onFailure(message: String) {
                _user.value = ValidationModel(message)
            }
        })
    }
}