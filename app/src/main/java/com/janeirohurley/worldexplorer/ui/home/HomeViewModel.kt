package com.janeirohurley.worldexplorer.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janeirohurley.worldexplorer.App
import com.janeirohurley.worldexplorer.data.*
import com.janeirohurley.worldexplorer.data.local.CountryDao
import com.janeirohurley.worldexplorer.data.local.DatabaseProvider
import com.janeirohurley.worldexplorer.data.local.toCountry
import com.janeirohurley.worldexplorer.data.local.toEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val dao: CountryDao = DatabaseProvider.getDatabase(App.instance).countryDao()
) : ViewModel() {

    private val _countries = MutableStateFlow<List<Country>>(emptyList())
    val countries: StateFlow<List<Country>> = _countries

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        observeLocalDb()
        refreshCountries()
    }

    private fun observeLocalDb() {
        viewModelScope.launch {
            dao.getAllCountries().collect { entities ->
                if (entities.isNotEmpty()) {
                    _countries.value = entities.map { it.toCountry() }
                    _isLoading.value = false
                }
            }
        }
    }

    fun refreshCountries() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiClient.api.getAllCountries()
                val countriesFromApi = response.map { it.toCountry() }

                // Récupérer les favoris actuels depuis la DB
                val localCountries = dao.getAllCountriesOnce() // méthode suspend pour récupérer une seule fois
                val localFavorites = localCountries.associateBy { it.name }

                // Fusionner : garder isFavorite local si existant
                val merged = countriesFromApi.map { apiCountry ->
                    val isFav = localFavorites[apiCountry.name]?.isFavorite ?: false
                    apiCountry.copy(isFavorite = isFav)
                }

                // Mise à jour Room avec les favoris préservés
                dao.insertAll(merged.map { it.toEntity() })

                _error.value = null
            } catch (e: Exception) {
                e.printStackTrace()
                if (_countries.value.isEmpty()) {
                    _error.value = "Impossible de charger les pays"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun toggleFavorite(country: Country) {
        viewModelScope.launch {
            val newValue = !country.isFavorite
            dao.updateFavorite(country.name, newValue)
        }
    }

    fun getCountryByName(name: String): Flow<Country?> {
        return dao.getCountryByName(name).map { it?.toCountry() }
    }



}
