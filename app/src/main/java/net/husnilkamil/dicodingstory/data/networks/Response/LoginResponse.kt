package net.husnilkamil.dicodingstory.data.networks.Response

import com.google.gson.annotations.SerializedName
import net.husnilkamil.dicodingstory.models.Auth

data class LoginResponse(

    @field:SerializedName("loginResult")
    val loginResult: Auth? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)


