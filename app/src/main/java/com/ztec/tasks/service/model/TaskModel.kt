package com.ztec.tasks.service.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat

class TaskModel {
    @SerializedName("id")
    var id = 0

    @SerializedName("user_id")
    var userId = 0

    @SerializedName("user_name")
    var userName = ""

    @SerializedName("company_id")
    var companyId = 0

    @SerializedName("company_name")
    var companyName = ""

    @SerializedName("scheduled_datetime")
    var scheduledDatetime = ""

    @SerializedName("description")
    var description = ""

    @SerializedName("value")
    var value = 0.0

    @SerializedName("comment")
    var comment: String? = null

    @SerializedName("device_token")
    var deviceToken = ""

    override fun toString(): String {
        return "[$companyName] - $userName \t\t ${SimpleDateFormat("HH:mm").format(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(scheduledDatetime))}\n$description" +
                "${if (comment.isNullOrEmpty()) "" else "\n" + comment }"
    }
}