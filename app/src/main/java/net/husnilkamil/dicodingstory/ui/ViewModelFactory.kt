package net.husnilkamil.dicodingstory.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.husnilkamil.dicodingstory.data.StoryRepository
import net.husnilkamil.dicodingstory.ui.addstory.AddStoryViewModel
import net.husnilkamil.dicodingstory.ui.home.StoryListViewModel
import net.husnilkamil.dicodingstory.utils.Injection

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryListViewModel(Injection.provideRepository(context)) as T
        }
        if(modelClass.isAssignableFrom(AddStoryViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return AddStoryViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

//class ViewModelFactory private constructor(private val repository: StoryRepository): ViewModelProvider.NewInstanceFactory() {
//    companion object {
//        @Volatile
//        private var instance: ViewModelFactory? = null
//
//        fun getInstance(context: Context): ViewModelFactory =
//            instance ?: synchronized(this) {
//                instance ?: ViewModelFactory(Injection.provideRepository(context)).apply {
//                    instance = this
//                }
//            }
//    }
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        when {
//            modelClass.isAssignableFrom(StoryListViewModel::class.java) -> {
//                return StoryListViewModel(repository) as T
//            }
//            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
//                return AddStoryViewModel(repository) as T
//            }
//            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
//        }
//
//    }
//
//}