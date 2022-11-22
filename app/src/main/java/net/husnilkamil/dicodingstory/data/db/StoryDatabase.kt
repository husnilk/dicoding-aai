package net.husnilkamil.dicodingstory.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.husnilkamil.dicodingstory.models.StoryItem

@Database(entities = [StoryItem::class], version = 1)
abstract class StoryDatabase : RoomDatabase() {

    abstract fun storyDao() : StoryDao

    companion object {
        @Volatile
        private var instance : StoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context) : StoryDatabase {
            if(instance == null){
                synchronized(StoryDatabase::class.java){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        StoryDatabase::class.java,
                        "story_db"
                    ).allowMainThreadQueries()
                        .build()
                }
            }
            return instance as StoryDatabase
        }
    }
}