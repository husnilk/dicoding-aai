package net.husnilkamil.dicodingstory.networks

import net.husnilkamil.dicodingstory.datamodels.DetailResponse
import net.husnilkamil.dicodingstory.datamodels.GetStoryResponse
import net.husnilkamil.dicodingstory.datamodels.LoginResponse
import net.husnilkamil.dicodingstory.datamodels.ObjectResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface DicodingStoryService {

    @FormUrlEncoded
    @POST("/v1/register")
    fun registerUser(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<ObjectResponse>

    @FormUrlEncoded
    @POST("/v1/login")
    fun loginUser(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<LoginResponse>


    @Multipart
    @POST("/v1/stories")
    fun addStories(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part?,
        @Part("description") description: RequestBody?
    ): Call<ObjectResponse>

    @GET("/v1/stories")
    fun getAllStories(
        @Header("Authorization") token: String,
        @Query("location") location: Int
    ): Call<GetStoryResponse>

    @GET("/v1/stories/{id}")
    fun getStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DetailResponse>
}