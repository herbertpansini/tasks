package com.ztec.tasks.service.repository.remote

import com.ztec.tasks.service.model.UserModel
import retrofit2.Call
import retrofit2.http.GET

interface UserService {
    @GET("users")
    fun list(): Call<List<UserModel>>
}