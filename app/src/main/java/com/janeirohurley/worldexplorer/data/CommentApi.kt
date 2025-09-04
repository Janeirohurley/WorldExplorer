package com.janeirohurley.worldexplorer.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CommentApi {

    // Récupère tous les commentaires ou ceux d'un pays précis
    @GET("test")
    suspend fun getAllComments(
        @Query("countryName") countryName: String? = null
    ): CommentsResponse

    @POST("test") // Remplace "comments" par le endpoint réel de ton serveur
    suspend fun postComment(@Body comment: CommentRequest)
}
