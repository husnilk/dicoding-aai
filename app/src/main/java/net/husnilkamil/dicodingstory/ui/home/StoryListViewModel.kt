package net.husnilkamil.dicodingstory.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import net.husnilkamil.dicodingstory.data.StoryRepository
import net.husnilkamil.dicodingstory.models.StoryItem

class StoryListViewModel(private val storyRepository: StoryRepository): ViewModel() {

    val stories : LiveData<PagingData<StoryItem>> = storyRepository.getAllStories().cachedIn(viewModelScope)
}

