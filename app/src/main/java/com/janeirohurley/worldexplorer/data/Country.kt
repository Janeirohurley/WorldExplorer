package com.janeirohurley.worldexplorer.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    val name: String,
    val officialName: String,
    val region: String,
    val capital: String,
    val population: Long,
    val flagUrl: String,
    val currency: String,
    val language: String,
    val isFavorite: Boolean = false
) : Parcelable

