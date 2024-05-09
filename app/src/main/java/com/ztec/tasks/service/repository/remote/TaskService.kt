package com.ztec.tasks.service.repository.remote

import com.ztec.tasks.service.model.TaskModel
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TaskService {
    @GET("tasks")
    fun list(@Query("user_id") userId: Int, @Query("scheduled_datetime") scheduledDatetime: String): Call<List<TaskModel>>

    @GET("tasks/{id}")
    fun load(@Path(value = "id", encoded = true) id: Int): Call<TaskModel>

    @GET("tasks/balance")
    fun balance(@Query("user_id") userId: Int,
                @Query("start_scheduled") startScheduled: String,
                @Query("end_scheduled") endScheduled: String
    ): Call<List<TaskModel>>

    @POST("tasks")
    @FormUrlEncoded
    fun create(
        @Field("company_id") companyId: Int,
        @Field("user_id") userId: Int,
        @Field("scheduled_datetime") scheduledDatetime: String,
        @Field("description") description: String,
        @Field("value") value: Double
    ): Call<TaskModel>

    @PUT("tasks/{id}")
    @FormUrlEncoded
    fun update(
        @Path(value = "id", encoded = true) id: Int,
        @Field("company_id") companyId: Int,
        @Field("user_id") userId: Int,
        @Field("scheduled_datetime") scheduledDatetime: String,
        @Field("description") description: String,
        @Field("value") value: Double,
        @Field("comment") comment: String?
    ): Call<TaskModel>

    @DELETE("tasks/{id}")
    fun delete(@Path(value = "id", encoded = true) id: Int): Call<Boolean>
}