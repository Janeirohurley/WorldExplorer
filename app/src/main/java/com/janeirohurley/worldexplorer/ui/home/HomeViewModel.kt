package com.janeirohurley.worldexplorer.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janeirohurley.worldexplorer.App
import com.janeirohurley.worldexplorer.data.*
import com.janeirohurley.worldexplorer.data.local.Comment
import com.janeirohurley.worldexplorer.data.local.CommentDao
import com.janeirohurley.worldexplorer.data.local.CountryDao
import com.janeirohurley.worldexplorer.data.local.DatabaseProvider
import com.janeirohurley.worldexplorer.data.local.toCountry
import com.janeirohurley.worldexplorer.data.local.toEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val countryDao: CountryDao = DatabaseProvider.getDatabase(App.instance).countryDao(),
    private val commentDao: CommentDao = DatabaseProvider.getDatabase(App.instance).commentDao()
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
            countryDao.getAllCountries().collect { entities ->
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
                // 1️⃣ Récupérer tous les pays depuis l'API
                val response = ApiClient.api.getAllCountries()
                val countriesFromApi = response.map { it.toCountry() }

                // 2️⃣ Récupérer les favoris actuels depuis la DB
                val localCountries = countryDao.getAllCountriesOnce() // suspend fonction pour fetch une seule fois
                val localFavorites = localCountries.associateBy { it.name }

                // 3️⃣ Fusionner les favoris : on garde isFavorite local si existant
                val merged = countriesFromApi.map { apiCountry ->
                    val isFav = localFavorites[apiCountry.name]?.isFavorite ?: false
                    apiCountry.copy(isFavorite = isFav)
                }

                // 4️⃣ Mise à jour Room
                countryDao.insertAll(merged.map { it.toEntity() })

                // 5️⃣ Clear l'erreur si succès
                _error.value = null
            } catch (e: Exception) {
                e.printStackTrace()
//                // 6️⃣ Afficher erreur seulement si la liste est vide
//                if (_countries.value.isEmpty()) {
//                    _error.value = "Impossible de charger les pays"
//                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(country: Country) {
        viewModelScope.launch {
            val newValue = !country.isFavorite
            countryDao.updateFavorite(country.name, newValue)
        }
    }

    fun getCountryByName(name: String): Flow<Country?> {
        return countryDao.getCountryByName(name).map { it?.toCountry() }
    }

//    ======================================================================

    fun getCommentsForCountry(countryName: String): Flow<List<Comment>> {
        return commentDao.getCommentsForCountry(countryName) // flow live
            .onStart {
                // si tu veux émettre les commentaires distants dès l’ouverture
                try {
                    val remoteComments = ApiClient.apicomment.getAllComments(countryName).comments

                    // 🔹 Debug : afficher chaque commentaire reçu
                    remoteComments.forEach { comment ->
                        Log.d("CommentsDebug", "Remote comment: countryName=${comment.countryName}, text=${comment.text}")
                        println("Remote comment: countryName=${comment.countryName}, text=${comment.text}")

                        commentDao.insertComment(
                            Comment(countryName = comment.countryName, text = comment.text)
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
    }




    fun addComment(countryName: String, text: String) {
        viewModelScope.launch {
            commentDao.insertComment(Comment(countryName = countryName, text = text))
        }
    }

    fun addCommentHybrid(countryName: String, text: String) {
        viewModelScope.launch {
//            // 1️⃣ Ajouter en local
//            val comment = Comment(countryName = countryName, text = text)
//            commentDao.insertComment(comment)

            addComment(countryName, text)

            // 2️⃣ Essayer d’envoyer à l’API externe
            try {
                val request = CommentRequest(countryName, text)
                ApiClient.apicomment.postComment(request)
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = "Commentaire sauvegardé localement (pas de connexion)"
            }
        }
    }





}
