package com.ztec.tasks.service.repository

import android.content.Context
import com.ztec.tasks.R
import com.ztec.tasks.service.listener.APIListener
import com.ztec.tasks.service.model.PersonModel
import com.ztec.tasks.service.repository.remote.PersonService
import com.ztec.tasks.service.repository.remote.RetrofitClient

class PersonRepository(context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.getService(PersonService::class.java)

    fun login(email: String, password: String, deviceToken: String, listener: APIListener<PersonModel>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.login(email, password, deviceToken), listener)
    }

    fun register(name: String, email: String, password: String, passwordConfirmation: String, deviceToken: String, listener: APIListener<PersonModel>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.register(name, email, password, passwordConfirmation, deviceToken), listener)
    }
}