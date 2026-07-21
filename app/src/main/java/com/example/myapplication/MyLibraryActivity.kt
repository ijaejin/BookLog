package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ui.booklist.MyLibraryAdapter
import com.example.myapplication.data.SavedBook
import com.example.myapplication.db.AppDatabase
import com.example.myapplication.repository.BookRepository
import com.example.myapplication.viewmodel.BookViewModel
import kotlinx.coroutines.launch

class MyLibraryActivity : AppCompatActivity() {

    private lateinit var bookViewModel: BookViewModel
    private lateinit var adapter: MyLibraryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_library)

        // AppDatabase와 Repository 연결
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = BookRepository(database.bookDao())

        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BookViewModel(repository) as T
            }
        }
        bookViewModel = ViewModelProvider(this, factory)[BookViewModel::class.java]

        adapter = MyLibraryAdapter(
            onItemClick = { book ->
                // 상태/메모 수정 다이얼로그
                Toast.makeText(this, "${book.title} 선택됨", Toast.LENGTH_SHORT).show()
            },
            onItemLongClick = { book -> // 길게 누를  삭제
                showDeleteDialog(book)
            }
        )

        // recyclerView 연결
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewMyLibrary)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        //데이터 관찰 & 리스트 업데이트
        lifecycleScope.launch {
            bookViewModel.myBooks.collect { books ->
                adapter.submitList(books)
            }
        }

        // 5. 내 서재 데이터 불러오기 실행
        bookViewModel.fetchMyBooks()
    }

    // 삭제 확인
    private fun showDeleteDialog(book: SavedBook) {
        AlertDialog.Builder(this)
            .setTitle("책 삭제")
            .setMessage("'${book.title}'을(를) 내 서재에서 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                bookViewModel.deleteBook(book)
                Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("취소", null)
            .show()
    }
}