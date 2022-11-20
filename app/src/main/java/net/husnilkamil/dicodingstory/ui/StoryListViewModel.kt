package net.husnilkamil.dicodingstory.ui

import androidx.lifecycle.ViewModel
import net.husnilkamil.dicodingstory.data.StoryRepository

class StoryListViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun getAllStories() = storyRepository.getAllStories()
}