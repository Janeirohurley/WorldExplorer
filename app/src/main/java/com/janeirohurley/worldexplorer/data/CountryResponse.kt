package com.janeirohurley.worldexplorer.data

data class CountryResponse(
    val name: Name,
    val capital: List<String>?,
    val region: String,
    val population: Long,
    val flags: Flags,
    val currencies: Map<String, Currency>?,
    val languages: Map<String, String>?
)

data class Name(val common: String, val official: String)
data class Flags(val png: String, val svg: String)
data class Currency(val name: String, val symbol: String)

fun CountryResponse.toCountry(): Country {
    return Country(
        name = name.common,
        officialName = name.official,
        region = region,
        capital = capital?.firstOrNull() ?: "N/A",
        population = population,
        flagUrl = flags.png,
        currency = currencies?.values?.firstOrNull()?.name ?: "N/A",
        language = languages?.values?.firstOrNull() ?: "N/A"
    )
}
