package net.husnilkamil.dicodingstory.data.networks

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkConfig {

//    private val client: Retrofit
//        get() = Retrofit.Builder()
//            .baseUrl("https://story-api.dicoding.dev/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()

    @JvmStatic
    val service: DicodingStoryService
        get() {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://quote-api.dicoding.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(DicodingStoryService::class.java)
//            val retrofit = client
//            return retrofit.create(DicodingStoryService::class.java)
        }
}