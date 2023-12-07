package com.ztec.tasks.service.repository

import android.content.Context
import com.ztec.tasks.R
import com.ztec.tasks.service.listener.APIListener
import com.ztec.tasks.service.model.TaskModel
import com.ztec.tasks.service.repository.remote.RetrofitClient
import com.ztec.tasks.service.repository.remote.TaskService

class TaskRepository(context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.getService(TaskService::class.java)

    fun list(userId: Int, scheduledDatetime: String, listener: APIListener<List<TaskModel>>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.list(userId, scheduledDatetime), listener)
    }

    fun load(id: Int, listener: APIListener<TaskModel>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.load(id), listener)
    }

    fun balance(userId: Int, startScheduled: String, endScheduled: String, listener: APIListener<List<TaskModel>>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.balance(userId, startScheduled, endScheduled), listener)
    }

    fun create(task: TaskModel, listener: APIListener<TaskModel>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.create(task.companyId, task.userId, task.scheduledDatetime, task.description, task.value)
        executeCall(call, listener)
    }

    fun update(task: TaskModel, listener: APIListener<TaskModel>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        val call = remote.update(task.id, task.companyId, task.userId, task.scheduledDatetime, task.description, task.value)
        executeCall(call, listener)
    }

    fun delete(id: Int, listener: APIListener<Boolean>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.delete(id), listener)
    }
}