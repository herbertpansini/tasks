package com.ztec.tarefas.service.repository

import android.content.Context
import com.ztec.tarefas.R
import com.ztec.tarefas.service.listener.APIListener
import com.ztec.tarefas.service.model.PersonModel
import com.ztec.tarefas.service.repository.remote.PersonService
import com.ztec.tarefas.service.repository.remote.RetrofitClient

class PersonRepository(context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.getService(PersonService::class.java)

    fun login(email: String, password: String, deviceToken: String, listener: APIListener<PersonModel>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.login(email, password, deviceToken), listener)
    }
}