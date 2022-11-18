package net.husnilkamil.dicodingstory.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import net.husnilkamil.dicodingstory.datamodels.StoryItem
import net.husnilkamil.dicodingstory.networks.DicodingStoryService

class StoryRepository (private val storyDatabase: StoryDatabase, private val service: DicodingStoryService){
    fun getStory(): LiveData<PagingData<StoryItem>> {
        return Pager(
            config = PagingConfig(pageSize =  5),
            pagingSourceFactory = {
                StoryPagingSource(service)
            }
        ).liveData
    }
}