package com.janeirohurley.worldexplorer.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://restcountries.com/"
    private const val BASE_URL_COMMENT = "http://192.168.3.230:5000/"

    val api: CountryApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountryApi::class.java)
    }
    val apicomment: CommentApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_COMMENT)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CommentApi::class.java)
    }
}
