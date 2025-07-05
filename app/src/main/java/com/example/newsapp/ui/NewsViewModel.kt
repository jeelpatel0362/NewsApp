package com.example.newsapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.RetrofitClient
import com.example.newsapp.model.NewsArticle
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val _newsArticles = MutableLiveData<List<NewsArticle>>()
    val newsArticles: LiveData<List<NewsArticle>> = _newsArticles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadTechNews() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = RetrofitClient.newsApiService.getTechNews()
                _newsArticles.value = response.data
            } catch (e: Exception) {
                _error.value = "Failed to load news: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
