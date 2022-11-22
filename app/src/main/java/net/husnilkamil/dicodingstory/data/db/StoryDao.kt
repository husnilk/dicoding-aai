package net.husnilkamil.dicodingstory.data.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import net.husnilkamil.dicodingstory.models.StoryItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(stories: List<StoryItem>)

    @Query("SELECT * FROM story ORDER BY id ASC")
    fun getAllStories() : PagingSource<Int, StoryItem>

    @Query("DELETE FROM story")
    fun deleteAll()



}
