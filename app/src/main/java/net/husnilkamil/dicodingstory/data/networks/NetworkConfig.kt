package net.husnilkamil.dicodingstory.data.networks

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkConfig {

    val client: Retrofit
        get() = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @JvmStatic
    val service: DicodingStoryService
        get() {
            val retrofit = client
            return retrofit.create(DicodingStoryService::class.java)
        }
}