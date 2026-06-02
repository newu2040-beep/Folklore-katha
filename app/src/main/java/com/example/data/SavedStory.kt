package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_stories")
data class SavedStory(
    @PrimaryKey val storyId: String,
    val titleNp: String,
    val titleEn: String,
    val author: String,
    val genre: String,
    val body: String,
    val excerpt: String,
    val readTimeMinutes: Int,
    val imageUrl: String? = null,
    val savedAt: Long = System.currentTimeMillis()
)
