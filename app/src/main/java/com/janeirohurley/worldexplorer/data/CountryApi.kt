package com.janeirohurley.worldexplorer.data

import retrofit2.http.GET
import retrofit2.http.Query

interface CountryApi {

    @GET("v3.1/all")
    suspend fun getAllCountries(
        @Query("fields") fields: String = "name,capital,region,population,flags,currencies,languages"
    ): List<CountryResponse>
}
