package com.example.newsapp.ui

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.model.NewsArticle

class NewsAdapter(private val onClick: (NewsArticle) -> Unit) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private val articles = mutableListOf<NewsArticle>()

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.articleTitle)
        private val imageView: ImageView = itemView.findViewById(R.id.articleImage)
        private val sourceTextView: TextView = itemView.findViewById(R.id.articleSource)
        private val dateTextView: TextView = itemView.findViewById(R.id.articleDate)
        private val authorTextView: TextView = itemView.findViewById(R.id.articleAuthor)

        fun bind(article: NewsArticle) {
            titleTextView.text = article.title
            sourceTextView.text = article.source_name ?: "Unknown source"
            dateTextView.text = article.getFormattedDate()
            authorTextView.text = article.getAuthorsText()

            Glide.with(itemView.context)
                .load(article.photo_url ?: article.thumbnail_url)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView)

            itemView.setOnClickListener { onClick(article) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount() = articles.size

    fun setArticles(newArticles: List<NewsArticle>) {
        articles.clear()
        articles.addAll(newArticles)
        notifyDataSetChanged()
    }

    fun addArticles(newArticles: List<NewsArticle>) {
        val startPosition = articles.size
        articles.addAll(newArticles)
        notifyItemRangeInserted(startPosition, newArticles.size)
    }
}