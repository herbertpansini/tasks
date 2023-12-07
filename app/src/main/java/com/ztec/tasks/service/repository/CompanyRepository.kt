package com.ztec.tasks.service.repository

import android.content.Context
import com.ztec.tasks.R
import com.ztec.tasks.service.listener.APIListener
import com.ztec.tasks.service.model.CompanyModel
import com.ztec.tasks.service.repository.remote.CompanyService
import com.ztec.tasks.service.repository.remote.RetrofitClient

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