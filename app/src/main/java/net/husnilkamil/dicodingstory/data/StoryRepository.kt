package net.husnilkamil.dicodingstory.data

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import net.husnilkamil.dicodingstory.helpers.getToken
import net.husnilkamil.dicodingstory.models.GetStoryResponse
import net.husnilkamil.dicodingstory.models.StoryItem
import net.husnilkamil.dicodingstory.networks.DicodingStoryService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository private constructor(
    private val service: DicodingStoryService,
    private val context: Context
) {
    private val result = MediatorLiveData<Result<List<StoryItem>>>()
    private val stories = MutableLiveData<List<StoryItem>>()

    fun getListStory() : LiveData<Result<List<StoryItem>>> {
        result.value = Result.Loading
        val service = service.getAllStories(getToken(context), 1)
        service.enqueue(object : Callback<GetStoryResponse?> {

            override fun onResponse(call: Call<GetStoryResponse?>, response: Response<GetStoryResponse?>) {
                var getStoryResponse : GetStoryResponse? = response.body()
                if(getStoryResponse != null){
                    val listStory = getStoryResponse.listStory
                    stories.value = (listStory as List<StoryItem>?)!!
                }
            }

            override fun onFailure(call: Call<GetStoryResponse?>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })

        result.addSource(stories) {
            newData: List<StoryItem> ->
                result.value = net.husnilkamil.dicodingstory.data.Result.Success(newData)

        }

        return result
    }
}
