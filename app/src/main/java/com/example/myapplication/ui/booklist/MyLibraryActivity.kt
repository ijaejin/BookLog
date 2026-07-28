package com.example.myapplication.ui.booklist

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.Book
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

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = BookRepository(database.bookDao())

        val factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BookViewModel(repository) as T
            }
        }

        bookViewModel =
            ViewModelProvider(this, factory)[BookViewModel::class.java]

        adapter = MyLibraryAdapter(
            onItemClick = { book ->
                showEditDialog(book)
            },
            onItemLongClick = { book ->
                showDeleteDialog(book)
            }
        )

        val recyclerView =
            findViewById<RecyclerView>(R.id.recyclerViewMyLibrary)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            bookViewModel.myBooks.collect { books ->
                adapter.submitList(books)
            }
        }

        bookViewModel.fetchMyBooks()
    }

    private fun showEditDialog(book: Book) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val inputStatus = EditText(this).apply {
            hint = "독서 상태 (예: 읽는 중, 완독)"
            setText(book.readingStatus)
        }
        layout.addView(inputStatus)

        val inputMemo = EditText(this).apply {
            hint = "감상 메모를 입력하세요"
            setText(book.memo)
        }
        layout.addView(inputMemo)

        AlertDialog.Builder(this)
            .setTitle("'${book.title}' 수정")
            .setView(layout)
            .setPositiveButton("저장") { _, _ ->
                val newStatus = inputStatus.text.toString().trim()
                val newMemo = inputMemo.text.toString().trim()

                bookViewModel.updateBookStatus(
                    book,
                    newStatus,
                    newMemo
                )

                Toast.makeText(
                    this,
                    "수정되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun showDeleteDialog(book: Book) {
        AlertDialog.Builder(this)
            .setTitle("책 삭제")
            .setMessage(
                "'${book.title}'을(를) 내 서재에서 삭제하시겠습니까?"
            )
            .setPositiveButton("삭제") { _, _ ->
                bookViewModel.deleteBook(book)

                Toast.makeText(
                    this,
                    "삭제되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("취소", null)
            .show()
    }
}