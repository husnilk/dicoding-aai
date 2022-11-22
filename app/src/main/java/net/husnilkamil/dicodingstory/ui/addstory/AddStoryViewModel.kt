package net.husnilkamil.dicodingstory.ui.addstory

import androidx.lifecycle.ViewModel
import net.husnilkamil.dicodingstory.data.StoryRepository
import net.husnilkamil.dicodingstory.data.networks.request.StoryRequest

class AddStoryViewModel (private val repository: StoryRepository): ViewModel(){
    fun uploadStory(storyRequest: StoryRequest) = repository.insert(storyRequest)
}