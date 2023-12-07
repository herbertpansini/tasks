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
import java.util.Calendar
import java.util.Date

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = _tasks

    fun list(selectedDate: Date = Calendar.getInstance().time) {
        taskRepository.list(securityPreferences.get(TaskConstants.USER.ID).toInt(),
                                                    SimpleDateFormat("yyyy-MM-dd").format(selectedDate),
                                                    object: APIListener<List<TaskModel>> {
            override fun onSuccess(result: List<TaskModel>) {
                _tasks.value = result
            }
            override fun onFailure(message: String) {}
        })
    }
}