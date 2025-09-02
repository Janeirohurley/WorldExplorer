package com.janeirohurley.worldexplorer.data.local

import com.janeirohurley.worldexplorer.data.Country

fun CountryEntity.toCountry() = Country(
    name = name,
    officialName = officialName,
    capital = capital,
    region = region,
    population = population,
    currency = currency,
    language = language,
    flagUrl = flagUrl,
    isFavorite = isFavorite
)

fun Country.toEntity() = CountryEntity(
    name = name,
    officialName = officialName,
    capital = capital,
    region = region,
    population = population,
    currency = currency,
    language = language,
    flagUrl = flagUrl,
    isFavorite = isFavorite
)
