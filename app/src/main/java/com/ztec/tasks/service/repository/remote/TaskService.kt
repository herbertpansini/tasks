package com.ztec.tasks.service.repository.remote

import com.ztec.tasks.service.model.TaskModel
import retrofit2.Call
import retrofit2.http.*

interface TaskService {

    @GET("tasks")
    fun list(@Query("user_id") userId: Int, @Query("scheduled_datetime") scheduledDatetime: String): Call<List<TaskModel>>

    @GET("tasks/balance")
    fun balance(@Query("user_id") userId: Int,
                @Query("start_scheduled") startScheduled: String,
                @Query("end_scheduled") endScheduled: String
    ): Call<List<TaskModel>>

    @GET("Task/Next7Days")
    fun listNext(): Call<List<TaskModel>>

    @GET("Task/Overdue")
    fun listOverdue(): Call<List<TaskModel>>

    @GET("tasks/{id}")
    fun load(@Path(value = "id", encoded = true) id: Int): Call<TaskModel>

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
        @Field("value") value: Double
    ): Call<TaskModel>

    @PUT("Task/Complete")
    @FormUrlEncoded
    fun complete(@Field("Id") id: Int): Call<Boolean>

    @PUT("Task/Undo")
    @FormUrlEncoded
    fun undo(@Field("Id") id: Int): Call<Boolean>

//    @HTTP(method = "DELETE", path = "tasks", hasBody = true)
//    @FormUrlEncoded
//    fun delete(@Field("id") id: Int): Call<Boolean>

    @DELETE("tasks/{id}")
    fun delete(@Path(value = "id", encoded = true) id: Int): Call<Boolean>
}