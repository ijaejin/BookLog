package com.example.myapplication.ui

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.adapter.BookAdapter
import com.example.myapplication.data.Book
import com.example.myapplication.data.RetrofitClient
import com.example.myapplication.data.SavedBook
import com.example.myapplication.db.AppDatabase
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private lateinit var adapter: BookAdapter
    private val bookList = mutableListOf<Book>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val etSearch = findViewById<EditText>(R.id.etSearch)
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val rvBooks = findViewById<RecyclerView>(R.id.rvBooks)

        adapter = BookAdapter(bookList) { book ->
            showBookDetailDialog(book)
        }
        rvBooks.layoutManager = LinearLayoutManager(this)
        rvBooks.adapter = adapter

        btnSearch.setOnClickListener {
            val query = etSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                searchBooks(query)
            } else {
                Toast.makeText(this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchBooks(query: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.kakaoBookApi.searchBooks(
                    apiKey = "KakaoAK ${BuildConfig.KAKAO_API_KEY}",
                    query = query
                )
                adapter.updateBooks(response.documents)
                if (response.documents.isEmpty()) {
                    Toast.makeText(this@SearchActivity, "검색 결과가 없습니다", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SearchActivity, "검색 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showBookDetailDialog(book: Book) {
        AlertDialog.Builder(this)
            .setTitle(book.title)
            .setMessage(
                "저자: ${book.authors.joinToString(", ")}\n" +
                        "출판사: ${book.publisher}\n" +
                        "가격: ${book.price}원\n\n" +
                        "${book.contents}"
            )
            .setPositiveButton("내 서재에 저장") { _, _ ->
                saveBookToLibrary(book)
            }
            .setNegativeButton("닫기", null)
            .show()
    }

    private fun saveBookToLibrary(book: Book) {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(this@SearchActivity)
            val savedBook = SavedBook(
                isbn = book.isbn,
                title = book.title,
                authors = book.authors.joinToString(", "),
                publisher = book.publisher,
                thumbnail = book.thumbnail,
                price = book.price,
                contents = book.contents
            )
            db.bookDao().insertBook(savedBook)
            Toast.makeText(this@SearchActivity, "내 서재에 저장했습니다", Toast.LENGTH_SHORT).show()
        }
    }
}