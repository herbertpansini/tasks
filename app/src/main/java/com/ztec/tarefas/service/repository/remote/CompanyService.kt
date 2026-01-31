package com.ztec.tarefas.service.repository.remote

import com.ztec.tarefas.service.model.CompanyModel
import retrofit2.Call
import retrofit2.http.GET

interface CompanyService {
    @GET("api/companies")
    fun list(): Call<List<CompanyModel>>
}