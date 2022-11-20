package net.husnilkamil.dicodingstory.data

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import net.husnilkamil.dicodingstory.data.db.StoryDao
import net.husnilkamil.dicodingstory.utils.getToken
import net.husnilkamil.dicodingstory.data.networks.Response.GetStoryResponse
import net.husnilkamil.dicodingstory.models.StoryItem
import net.husnilkamil.dicodingstory.data.networks.DicodingStoryService
import net.husnilkamil.dicodingstory.data.networks.NetworkConfig
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
    private val result = MediatorLiveData<Result<List<StoryItem>>>()

    fun getAllStories() : LiveData<Result<List<StoryItem>>>{
        result.value = Result.Loading

        val client = apiService.getAllStories(getToken(context), 1)
        client.enqueue(object: Callback<GetStoryResponse> {
            override fun onResponse(call: Call<GetStoryResponse>, response: Response<GetStoryResponse>) {
                var getStoryResponse : GetStoryResponse? = response.body()
                if(getStoryResponse != null){
                    val listStory = getStoryResponse.listStory
                    storyDao.deleteAll()
                    storyDao.insertAll(listStory);
                }
            }

            override fun onFailure(call: Call<GetStoryResponse?>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        val localData = storyDao.getAllStories()
        result.addSource(localData){ newData: List<StoryItem> ->
            result.value = Result.Success(newData)
        }

        return result
    }

    fun insert(storyItem: StoryItem){

    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: DicodingStoryService,
            storyDao: StoryDao,
            appExecutors: AppExecutors,
            context: Context): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, storyDao, appExecutors, context)
            }.also {
                instance = it
            }
    }

}