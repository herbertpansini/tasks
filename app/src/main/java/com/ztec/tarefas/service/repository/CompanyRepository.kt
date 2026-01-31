package com.ztec.tarefas.service.repository

import android.content.Context
import com.ztec.tarefas.R
import com.ztec.tarefas.service.listener.APIListener
import com.ztec.tarefas.service.model.CompanyModel
import com.ztec.tarefas.service.repository.remote.CompanyService
import com.ztec.tarefas.service.repository.remote.RetrofitClient

class CompanyRepository(context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.getService(CompanyService::class.java)

    fun list(listener: APIListener<List<CompanyModel>>) {
        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.list(), listener)
    }
}