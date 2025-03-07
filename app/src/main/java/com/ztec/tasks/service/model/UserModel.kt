package com.ztec.tasks.service.model

import com.google.gson.annotations.SerializedName

class UserModel {
    @SerializedName("id")
    val id = 0
    @SerializedName("name")
    var name = ""
    @SerializedName("email")
    var email = ""
    @SerializedName("password")
    var password = ""
    @SerializedName("password_confirmation")
    var passwordConfirmation = ""
    @SerializedName("role")
    var role = ""
    @SerializedName("device_token")
    var deviceToken = ""
}