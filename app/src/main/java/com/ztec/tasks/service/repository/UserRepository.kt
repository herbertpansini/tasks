package com.ztec.tasks.service.repository

import android.content.Context
import com.ztec.tasks.R
import com.ztec.tasks.service.listener.APIListener
import com.ztec.tasks.service.model.UserModel
import com.ztec.tasks.service.repository.remote.RetrofitClient
import com.ztec.tasks.service.repository.remote.UserService

class UserRepository(context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.getService(UserService::class.java)

    fun list(listener: APIListener<List<UserModel>>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.list(), listener)
    }

    fun register(user: UserModel, listener: APIListener<UserModel>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.register(user), listener)
    }
}