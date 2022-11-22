package net.husnilkamil.dicodingstory.data.networks.request

import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRequest (val token: String, val description: RequestBody, val file: MultipartBody.Part ){
}