package com.janeirohurley.worldexplorer.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {
    @Query("SELECT * FROM countries")
    suspend fun getAllCountriesOnce(): List<CountryEntity>
    @Query("SELECT * FROM countries")
    fun getAllCountries(): Flow<List<CountryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countries: List<CountryEntity>)

    @Update
    suspend fun updateCountry(country: CountryEntity)

    @Query("UPDATE countries SET isFavorite = :isFavorite WHERE name = :countryName")
    suspend fun updateFavorite(countryName: String, isFavorite: Boolean)

    @Query("SELECT * FROM countries WHERE name = :name LIMIT 1")
    fun getCountryByName(name: String): Flow<CountryEntity?>

    @Query("DELETE FROM countries")
    suspend fun clearAll()
}
