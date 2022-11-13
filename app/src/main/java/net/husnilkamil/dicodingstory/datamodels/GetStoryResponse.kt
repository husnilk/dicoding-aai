package net.husnilkamil.dicodingstory.datamodels

import com.google.gson.annotations.SerializedName

data class GetStoryResponse(

    @field:SerializedName("listStory")
    val listStory: List<StoryItem?>? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

