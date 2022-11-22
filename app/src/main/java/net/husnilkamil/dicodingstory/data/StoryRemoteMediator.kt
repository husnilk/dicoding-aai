package net.husnilkamil.dicodingstory.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import net.husnilkamil.dicodingstory.data.db.StoryDatabase
import net.husnilkamil.dicodingstory.data.networks.DicodingStoryService
import net.husnilkamil.dicodingstory.models.StoryItem

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator (private val database: StoryDatabase, private val apiService: DicodingStoryService):
    RemoteMediator<Int, StoryItem>() {
    override suspend fun load( loadType: LoadType, state: PagingState<Int, StoryItem>): MediatorResult {

        val page = INITIAL_PAGE_INDEX

        try {
            val responseData = apiService.getAllStories("", 1)
            val listStory: List<StoryItem> = responseData.listStory

            val endOfPaginationReached = listStory.isEmpty()
            database.withTransaction{
                if (loadType == LoadType.REFRESH) {
                    database.storyDao().deleteAll()
                }
                database.storyDao().insertAll(listStory)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }

    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
}