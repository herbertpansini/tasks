package com.ztec.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ztec.tasks.service.listener.APIListener
import com.ztec.tasks.service.model.CompanyModel
import com.ztec.tasks.service.model.TaskModel
import com.ztec.tasks.service.model.UserModel
import com.ztec.tasks.service.model.ValidationModel
import com.ztec.tasks.service.repository.CompanyRepository
import com.ztec.tasks.service.repository.TaskRepository
import com.ztec.tasks.service.repository.UserRepository
import com.ztec.tasks.service.repository.remote.FCMSend

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val companyRepository = CompanyRepository(application.applicationContext)
    private val userRepository = UserRepository(application.applicationContext)
    private val taskRepository = TaskRepository(application.applicationContext)
    private val fcmSend = FCMSend(application.applicationContext)

    private val _companyList = MutableLiveData<List<CompanyModel>>()
    val companyList: LiveData<List<CompanyModel>> = _companyList

    private val _userList = MutableLiveData<List<UserModel>>()
    val userList: LiveData<List<UserModel>> = _userList

    private val _taskSave = MutableLiveData<ValidationModel>()
    val taskSave: LiveData<ValidationModel> = _taskSave

    private val _task = MutableLiveData<TaskModel>()
    val task: LiveData<TaskModel> = _task

    private val _taskLoad = MutableLiveData<ValidationModel>()
    val taskLoad: LiveData<ValidationModel> = _taskLoad

    private val _delete = MutableLiveData<ValidationModel>()
    val delete: LiveData<ValidationModel> = _delete

    fun loadCompanies() {
        companyRepository.list(object: APIListener<List<CompanyModel>> {
            override fun onSuccess(result: List<CompanyModel>) {
                _companyList.value = result
            }
            override fun onFailure(message: String) {}
        })
    }

    fun loadUsers() {
        userRepository.list(object: APIListener<List<UserModel>> {
            override fun onSuccess(result: List<UserModel>) {
                _userList.value = result
            }
            override fun onFailure(message: String) {}
        })
    }

    fun load(id: Int) {
        taskRepository.load(id, object : APIListener<TaskModel> {
            override fun onSuccess(result: TaskModel) {
                _task.value = result
            }
            override fun onFailure(message: String) {
                _taskLoad.value = ValidationModel(message)
            }
        })
    }

    fun save(task: TaskModel) {
        val listener = object: APIListener<TaskModel>{
            override fun onSuccess(result: TaskModel) {
                fcmSend.pushNotification(result.deviceToken, "ZTec", result.toString())

                _taskSave.value = ValidationModel()
            }
            override fun onFailure(message: String) {
                _taskSave.value = ValidationModel(message)
            }
        }

        if (task.id == 0) {
            taskRepository.create(task, listener)
        } else {
            taskRepository.update(task, listener)
        }
    }

    fun delete(id: Int) {
        taskRepository.delete(id, object : APIListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                _delete.value = ValidationModel()
            }

            override fun onFailure(message: String) {
                _delete.value = ValidationModel(message)
            }
        })
    }
}