package net.husnilkamil.dicodingstory.utils

import android.content.Context
import net.husnilkamil.dicodingstory.data.StoryRepository
import net.husnilkamil.dicodingstory.data.db.StoryDatabase
import net.husnilkamil.dicodingstory.data.networks.NetworkConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = NetworkConfig.service
        val database = StoryDatabase.getDatabase(context)
        val dao = database.storyDao()
        val appExecutors = AppExecutors()
        return StoryRepository.getInstance(apiService, dao, appExecutors, context)
    }
}
