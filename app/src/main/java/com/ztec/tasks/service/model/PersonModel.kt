package com.ztec.tasks.service.model

import com.google.gson.annotations.SerializedName

class PersonModel {
    @SerializedName("id")
    var id = 0

    @SerializedName("name")
    var name = ""

    @SerializedName("role")
    var role = ""

    @SerializedName("token")
    var token = ""
}