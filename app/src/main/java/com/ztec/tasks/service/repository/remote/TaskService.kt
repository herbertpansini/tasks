package com.ztec.tasks.service.repository.remote

import com.ztec.tasks.service.model.TaskModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TaskService {
    @GET("api/tasks")
    fun list(@Query("user_id") userId: Int, @Query("scheduled_datetime") scheduledDatetime: String): Call<List<TaskModel>>

    @GET("api/tasks/{id}")
    fun load(@Path(value = "id", encoded = true) id: Int): Call<TaskModel>

    @GET("api/tasks/balance")
    fun balance(@Query("user_id") userId: Int,
                @Query("start_scheduled") startScheduled: String,
                @Query("end_scheduled") endScheduled: String
    ): Call<List<TaskModel>>

    @Headers("Content-Type: application/json")
    @POST("api/tasks")
    fun create(@Body task: TaskModel): Call<TaskModel>

    @Headers("Content-Type: application/json")
    @PUT("api/tasks/{id}")
    fun update(
        @Path(value = "id", encoded = true) id: Int,
        @Body task: TaskModel): Call<TaskModel>

    @DELETE("api/tasks/{id}")
    fun delete(@Path(value = "id", encoded = true) id: Int): Call<Boolean>
}