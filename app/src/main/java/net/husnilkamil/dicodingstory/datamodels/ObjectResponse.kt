package net.husnilkamil.dicodingstory.datamodels

import com.google.gson.annotations.SerializedName

data class ObjectResponse(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null

)
