package com.ztec.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ztec.tasks.service.constants.TaskConstants
import com.ztec.tasks.service.repository.SecurityPreferences

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    fun logout() {
        securityPreferences.remove(TaskConstants.USER.ID)
        securityPreferences.remove(TaskConstants.USER.NAME)
        securityPreferences.remove(TaskConstants.USER.EMAIL)
        securityPreferences.remove(TaskConstants.USER.PASSWORD)
        securityPreferences.remove(TaskConstants.USER.DEVICE)
        securityPreferences.remove(TaskConstants.USER.ROLE)

        securityPreferences.remove(TaskConstants.HEADER.TOKEN_VALUE)
    }

    fun loadUserName() {
        _name.value = securityPreferences.get(TaskConstants.USER.NAME)
    }
}