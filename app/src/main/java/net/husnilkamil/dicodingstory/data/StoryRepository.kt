package net.husnilkamil.dicodingstory.data

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import net.husnilkamil.dicodingstory.data.db.StoryDao
import net.husnilkamil.dicodingstory.utils.getToken
import net.husnilkamil.dicodingstory.data.networks.Response.GetStoryResponse
import net.husnilkamil.dicodingstory.models.StoryItem
import net.husnilkamil.dicodingstory.data.networks.DicodingStoryService
import net.husnilkamil.dicodingstory.data.networks.Response.ObjectResponse
import net.husnilkamil.dicodingstory.data.networks.request.StoryRequest
import net.husnilkamil.dicodingstory.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository private constructor(
    private val apiService: DicodingStoryService,
    private val storyDao: StoryDao,
    private val executors: AppExecutors,
    private val context: Context
) {
    private val resultAllStories = MediatorLiveData<List<StoryItem>>()
    private val resultAddStory = MediatorLiveData<ObjectResponse>()

    fun getAllStories(): LiveData<List<StoryItem>> {

        val client = apiService.getAllStories(getToken(context), 1)
        client.enqueue(object : Callback<GetStoryResponse> {
            override fun onResponse(
                call: Call<GetStoryResponse>,
                response: Response<GetStoryResponse>
            ) {
                var getStoryResponse: GetStoryResponse? = response.body()
                if (getStoryResponse != null) {
                    val listStory = getStoryResponse.listStory
                    storyDao.deleteAll()
                    storyDao.insertAll(listStory);
                }
            }

            override fun onFailure(call: Call<GetStoryResponse?>, t: Throwable) {
            }
        })
        val localData = storyDao.getAllStories()
        resultAllStories.addSource(localData) { newData: List<StoryItem> ->
            resultAllStories.value = newData
        }

        return resultAllStories
    }

    fun insert(storyRequest: StoryRequest): LiveData<ObjectResponse> {
        val response = apiService.addStories(
            "Bearer $storyRequest.token",
            storyRequest.file,
            storyRequest.description
        )
        response.enqueue(object : Callback<ObjectResponse?> {

            override fun onResponse(
                call: Call<ObjectResponse?>,
                response: Response<ObjectResponse?>
            ) {
                val objectResponse: ObjectResponse? = response.body()
                if (objectResponse != null && !objectResponse.error!!) {
                    if (!objectResponse.error) {
                        resultAddStory.value = objectResponse!!
                    }
                }
            }

            override fun onFailure(call: Call<ObjectResponse?>, t: Throwable) {
                resultAddStory.value = ObjectResponse(true, t.message.toString());
            }
        })
        return resultAddStory
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: DicodingStoryService,
            storyDao: StoryDao,
            appExecutors: AppExecutors,
            context: Context
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, storyDao, appExecutors, context)
            }.also {
                instance = it
            }
    }

}