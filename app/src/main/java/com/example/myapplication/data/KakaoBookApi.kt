package com.example.myapplication.data

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoBookApi {
    @GET("v3/search/book")
    suspend fun searchBooks(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): BookResponse
}