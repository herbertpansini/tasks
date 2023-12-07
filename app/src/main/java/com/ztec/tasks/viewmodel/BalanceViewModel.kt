package com.ztec.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ztec.tasks.service.constants.TaskConstants
import com.ztec.tasks.service.listener.APIListener
import com.ztec.tasks.service.model.TaskModel
import com.ztec.tasks.service.repository.SecurityPreferences
import com.ztec.tasks.service.repository.TaskRepository
import java.text.SimpleDateFormat
import java.util.Date

class BalanceViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _balance = MutableLiveData<List<TaskModel>>()
    val balance: LiveData<List<TaskModel>> = _balance

    fun balance(startScheduled: Date, endScheduled: Date) {
        taskRepository.balance(securityPreferences.get(TaskConstants.USER.ID).toInt(),
                                                        SimpleDateFormat("yyyy-MM-dd").format(startScheduled),
                                                        SimpleDateFormat("yyyy-MM-dd").format(endScheduled),
                                                        object: APIListener<List<TaskModel>> {
            override fun onSuccess(result: List<TaskModel>) {
                _balance.value = result
            }
            override fun onFailure(message: String) {}
        })
    }
}