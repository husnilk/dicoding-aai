package net.husnilkamil.dicodingstory.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.husnilkamil.dicodingstory.models.StoryItem
import net.husnilkamil.dicodingstory.networks.DicodingStoryService

class StoryPagingSource(private val service: DicodingStoryService) : PagingSource<Int, StoryItem >(){

    private companion object{
        const val INITIAL_IDX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, StoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryItem> {
        return try{
            val position = params.key ?: INITIAL_IDX
            val response = service.getAllStories("", params.loadSize)
            val responseData = response.listStory

            LoadResult.Page(
                data = responseData,
                prevKey =  if(position == INITIAL_IDX) null else position - 1,
                nextKey =  if(responseData.isNullOrEmpty()) null else position + 1
            )

        }catch (exception: Exception){
            return LoadResult.Error(exception)
        }
    }
}