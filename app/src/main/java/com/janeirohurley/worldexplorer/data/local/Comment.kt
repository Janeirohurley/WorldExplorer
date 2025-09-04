package com.janeirohurley.worldexplorer.data.local
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val countryName: String,
    val text: String
)
