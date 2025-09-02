package com.janeirohurley.worldexplorer.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey val name: String,
    val officialName: String,
    val capital: String,
    val region: String,
    val population: Long,
    val currency: String,
    val language: String,
    val flagUrl: String,
    val isFavorite: Boolean = false
)
