package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.SavedBook
import com.example.myapplication.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository) : ViewModel() {
    // 화면에 보여줄 내 서재 목록 (상태가 변할 때마다 화면에 알림)
    private val _myBooks = MutableStateFlow<List< SavedBook>>(emptyList())
    val myBooks: StateFlow<List<SavedBook>> = _myBooks

    // 내 서재 목록 로드
    fun fetchMyBooks() {
        viewModelScope.launch {
            val books = repository.getAllBooks() // 여기도 괄호 비우기
            _myBooks.value = books
        }
    }

    // 책 저장
    fun insertBook(book: SavedBook) {
        viewModelScope.launch {
            repository.insertBook(book)
            fetchMyBooks()
        }
    }

    // 책 상태 업데이트
    fun updateBookStatus(book: SavedBook, newStatus: String, newMemo: String) {
        val newCompletedDate = if (newStatus == "완독") {
            if(book.readingStatus != "완독") System.currentTimeMillis().toString() else book.completedDate
        } else {
            ""
        }

        // 기존 책 정보에서 상태, 메모, 완독일만 쏙 바꿔서 새로운 객체 생성
        val updatedBook = book.copy(
            readingStatus = newStatus,
            memo = newMemo,
            completedDate = newCompletedDate
        )

        viewModelScope.launch {
            repository.updateBook(updatedBook)
            fetchMyBooks()
        }
    }

    // 책 삭제
    fun deleteBook(book: SavedBook) {
        viewModelScope.launch {
            repository.deleteBook(book)
            fetchMyBooks()
        }
    }
}