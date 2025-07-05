package com.example.newsapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.RetrofitClient
import com.example.newsapp.model.NewsArticle
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    var currentPage = 1
    val pageSize = 10
    var isLastPage = false

    private val _newsArticles = MutableLiveData<List<NewsArticle>>()
    val newsArticles: LiveData<List<NewsArticle>> = _newsArticles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _shouldShowNextButton = MutableLiveData<Boolean>()
    val shouldShowNextButton: LiveData<Boolean> = _shouldShowNextButton

    init {
        loadTechNews()
    }

    fun loadTechNews() {
        currentPage = 1
        isLastPage = false
        _isLoading.value = true
        _shouldShowNextButton.value = false

        viewModelScope.launch {
            try {
                val response = RetrofitClient.newsApiService.getTechNews(page = currentPage)
                _newsArticles.value = response.data
                isLastPage = response.data.size < pageSize
                currentPage++
            } catch (e: Exception) {
                _error.value = "Failed to load news: ${e.message}"
            } finally {
                _isLoading.value = false
                _shouldShowNextButton.value = !isLastPage && !_newsArticles.value.isNullOrEmpty()
            }
        }
    }

    fun loadNextPage() {
        if (isLoading.value == true || isLastPage) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.newsApiService.getTechNews(page = currentPage)
                val currentArticles = _newsArticles.value ?: emptyList()
                _newsArticles.value = currentArticles + response.data

                isLastPage = response.data.size < pageSize
                currentPage++
            } catch (e: Exception) {
                _error.value = "Failed to load more news: ${e.message}"
            } finally {
                _isLoading.value = false
                _shouldShowNextButton.value = !isLastPage && !_newsArticles.value.isNullOrEmpty()
            }
        }
    }
}
