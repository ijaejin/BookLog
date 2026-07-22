package com.example.myapplication

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
                // 레이아웃 구성
                val context = this@MyLibraryActivity
                val layout = LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(50, 40, 50, 10)
                }

                // 독서 상태
                val inputStatus = EditText(context).apply {
                    hint = "독서 상태 (예: 읽는 중, 완독)"
                    setText(book.readingStatus) // 기존에 저장되어 있던 상태를 기본으로 보여줌
                }
                layout.addView(inputStatus)

                // 감상 메모
                val inputMemo = EditText(context).apply {
                    hint = "감상 메모를 입력하세요"
                    setText(book.memo ?: "") // 기존 메모
                }
                layout.addView(inputMemo)

                // AlertDialog
                AlertDialog.Builder(context)
                    .setTitle("'${book.title}' 수정")
                    .setView(layout)
                    .setPositiveButton("저장") { _, _ ->
                        val newStatus = inputStatus.text.toString()
                        val newMemo = inputMemo.text.toString()


                        // DB 업데이트 함수 호출
                        bookViewModel.updateBookSatus(book, newStatus, newMemo)
                        Toast.makeText(context, "수정되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("취소", null)
                    .show()
            },
            onItemLongClick = { book -> // 길게 누를 때 삭제
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