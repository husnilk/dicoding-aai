package net.husnilkamil.dicodingstory.data.networks.Response

import com.google.gson.annotations.SerializedName
import net.husnilkamil.dicodingstory.models.StoryItem

data class GetStoryResponse(

    @field:SerializedName("listStory")
    val listStory: List<StoryItem>,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

