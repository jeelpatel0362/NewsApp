package com.example.newsapp.data

import com.example.newsapp.model.NewsResponse
import com.example.newsapp.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NewsApiService {
    @GET("topic-news-by-section")
    suspend fun getTechNews(
        @Query("topic") topic: String = "TECHNOLOGY",
        @Query("section") section: String = "CAQiSkNCQVNNUW9JTDIwdk1EZGpNWFlTQldWdUxVZENHZ0pKVENJT0NBUWFDZ29JTDIwdk1ETnliSFFxQ2hJSUwyMHZNRE55YkhRb0FBKi4IACoqCAoiJENCQVNGUW9JTDIwdk1EZGpNWFlTQldWdUxVZENHZ0pKVENnQVABUAE",
        @Query("limit") limit: Int = 20,
        @Query("country") country: String = "IN",
        @Query("lang") lang: String = "en",
        @Header("x-rapidapi-host") host: String = Constants.API_HOST,
        @Header("x-rapidapi-key") key: String = Constants.API_KEY
    ): NewsResponse
}