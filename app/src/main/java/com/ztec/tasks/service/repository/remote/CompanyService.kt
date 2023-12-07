package com.ztec.tasks.service.repository.remote

import com.ztec.tasks.service.model.CompanyModel
import retrofit2.Call
import retrofit2.http.GET

interface CompanyService {
    @GET("companies")
    fun list(): Call<List<CompanyModel>>
}