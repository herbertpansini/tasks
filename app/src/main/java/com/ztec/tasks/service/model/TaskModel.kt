package com.ztec.tasks.service.model

import com.google.gson.annotations.SerializedName

class TaskModel {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("company_id")
    var companyId = 0

    @SerializedName("company")
    var companyModel: CompanyModel = CompanyModel()

    @SerializedName("user_id")
    var userId = 0

    @SerializedName("user")
    var userModel: UserModel = UserModel()

    @SerializedName("scheduled_datetime")
    var scheduledDatetime: String = ""

    @SerializedName("description")
    var description: String = ""

    @SerializedName("value")
    var value: Double = 0.0
}