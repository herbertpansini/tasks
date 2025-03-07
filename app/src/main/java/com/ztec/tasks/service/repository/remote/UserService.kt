package com.ztec.tasks.service.repository.remote

import com.ztec.tasks.service.model.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserService {
    @GET("api/users")
    fun list(): Call<List<UserModel>>

    @Headers("Content-Type: application/json")
    @POST("api/users")
    fun register(@Body user: UserModel): Call<UserModel>
}