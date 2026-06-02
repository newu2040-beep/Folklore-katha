package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedStoryDao {
    @Query("SELECT * FROM saved_stories ORDER BY savedAt DESC")
    fun getAllSavedStories(): Flow<List<SavedStory>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_stories WHERE storyId = :storyId LIMIT 1)")
    fun isStorySaved(storyId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedStory(story: SavedStory)

    @Query("DELETE FROM saved_stories WHERE storyId = :storyId")
    suspend fun deleteSavedStoryById(storyId: String)
}
