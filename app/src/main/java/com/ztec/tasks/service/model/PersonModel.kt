package com.ztec.tasks.service.model

import com.google.gson.annotations.SerializedName

class PersonModel {
    @SerializedName("id")
    lateinit var id: String

    @SerializedName("name")
    lateinit var name: String

    @SerializedName("role")
    lateinit var role: String

    @SerializedName("token")
    lateinit var token: String
}