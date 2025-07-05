package com.example.newsapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R

class MainActivity : AppCompatActivity() {
    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: NewsAdapter
    private lateinit var viewModel: NewsViewModel
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nextButton = findViewById(R.id.nextButton)
        newsRecyclerView = findViewById(R.id.newsRecyclerView)
        progressBar = findViewById(R.id.progressBar)

        setupRecyclerView()
        setupViewModel()
    }

    private fun setupRecyclerView() {
        adapter = NewsAdapter { article ->
            article.source_url?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }
        newsRecyclerView.layoutManager = LinearLayoutManager(this)
        newsRecyclerView.adapter = adapter
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        viewModel.loadTechNews()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.newsArticles.observe(this) { articles ->
            if (viewModel.currentPage == 1) {
                adapter.setArticles(articles)
            } else {
                adapter.addArticles(articles)
            }
            updateNextButtonVisibility()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            nextButton.isEnabled = !isLoading
            updateNextButtonVisibility()
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                updateNextButtonVisibility()
            }
        }

        nextButton.setOnClickListener {
            viewModel.loadNextPage()
        }
    }

    private fun updateNextButtonVisibility() {
        val shouldShow = !viewModel.isLastPage &&
                !viewModel.isLoading.value!! &&
                !viewModel.newsArticles.value.isNullOrEmpty()
        nextButton.visibility = if (shouldShow) View.VISIBLE else View.GONE
    }
}