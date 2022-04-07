package com.apolis.myapplication.metadata

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val code: Int,
    @SerializedName("data")
    val userList: List<User>,
    val meta: Meta
)