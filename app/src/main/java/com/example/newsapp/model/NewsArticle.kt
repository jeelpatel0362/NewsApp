package com.example.newsapp.model

import java.text.SimpleDateFormat
import java.util.Locale

data class NewsArticle(
    val title: String,
    val photo_url: String?,
    val thumbnail_url: String?,
    val published_datetime_utc: String,
    val authors: List<String>?,
    val source_url: String?,
    val source_name: String?
) {
    fun getFormattedDate(): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
            val date = inputFormat.parse(published_datetime_utc)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            published_datetime_utc
        }
    }

    fun getAuthorsText(): String {
        return if (authors.isNullOrEmpty()) {
            "Unknown author"
        } else {
            authors.joinToString(", ")
        }
    }
}

data class NewsResponse(
    val status: String,
    val request_id: String,
    val data: List<NewsArticle>
)