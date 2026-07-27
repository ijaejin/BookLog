package com.example.myapplication.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.adapter.BookAdapter
import com.example.myapplication.data.Book
import com.example.myapplication.data.BookItem
import com.example.myapplication.data.RetrofitClient
import com.example.myapplication.db.AppDatabase
import kotlinx.coroutines.launch
import java.io.IOException

class SearchActivity : AppCompatActivity() {

    private lateinit var adapter: BookAdapter
    private lateinit var etSearch: EditText
    private lateinit var tvEmpty: TextView
    private val bookList = mutableListOf<BookItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        etSearch = findViewById(R.id.etSearch)
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val rvBooks = findViewById<RecyclerView>(R.id.rvBooks)
        tvEmpty = findViewById(R.id.tvEmpty)

        adapter = BookAdapter(bookList) { book ->
            showBookDetailDialog(book)
        }
        rvBooks.layoutManager = LinearLayoutManager(this)
        rvBooks.adapter = adapter

        btnSearch.setOnClickListener {
            val query = etSearch.text.toString().trim()
            if (query.isBlank()) {
                etSearch.error = "검색어를 입력해주세요"
                return@setOnClickListener
            }
            searchBooks(query)
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
                tvEmpty.visibility = if (response.documents.isEmpty()) View.VISIBLE else View.GONE

            } catch (e: IOException) {
                // 네트워크 연결 자체가 안 되는 경우 (오프라인, 타임아웃 등)
                Toast.makeText(
                    this@SearchActivity,
                    "인터넷 연결을 확인해주세요",
                    Toast.LENGTH_SHORT
                ).show()

            } catch (e: retrofit2.HttpException) {
                // 서버가 에러 응답을 준 경우 (401 인증 실패, 500 서버 오류 등)
                val message = when (e.code()) {
                    401 -> "API 인증에 실패했습니다. 관리자에게 문의해주세요"
                    else -> "서버 오류가 발생했습니다 (${e.code()})"
                }
                Toast.makeText(this@SearchActivity, message, Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                // 그 외 예상 못 한 에러
                Toast.makeText(
                    this@SearchActivity,
                    "알 수 없는 오류가 발생했습니다: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showBookDetailDialog(book: BookItem) {
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

    private fun saveBookToLibrary(bookItem: BookItem) {
        lifecycleScope.launch {
            try {
                val db = AppDatabase.getDatabase(this@SearchActivity)

                val alreadySaved = db.bookDao().isBookSaved(bookItem.isbn)
                if (alreadySaved) {
                    Toast.makeText(this@SearchActivity, "이미 내 서재에 있는 책입니다", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val book = Book(
                    isbn = bookItem.isbn,
                    title = bookItem.title,
                    author = bookItem.authors.joinToString(", "),
                    publisher = bookItem.publisher,
                    coverUrl = bookItem.thumbnail,
                    contents = bookItem.contents,
                    price = bookItem.price
                )
                db.bookDao().insertBook(book)
                Toast.makeText(this@SearchActivity, "내 서재에 저장했습니다", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Toast.makeText(this@SearchActivity, "저장 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }
}