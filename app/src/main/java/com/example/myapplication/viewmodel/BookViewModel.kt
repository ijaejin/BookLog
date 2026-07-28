package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Book
import com.example.myapplication.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookViewModel(
    private val repository: BookRepository
) : ViewModel() {

    private val _myBooks = MutableStateFlow<List<Book>>(emptyList())
    val myBooks: StateFlow<List<Book>> = _myBooks

    fun fetchMyBooks() {
        viewModelScope.launch {
            _myBooks.value = repository.getAllBooks()
        }
    }

    fun insertBook(book: Book) {
        viewModelScope.launch {
            repository.insertBook(book)
            fetchMyBooks()
        }
    }

    fun updateBookStatus(
        book: Book,
        newStatus: String,
        newMemo: String
    ) {
        val newCompletedDate: Long? =
            if (newStatus == "완독") {
                if (book.readingStatus != "완독") {
                    System.currentTimeMillis()
                } else {
                    book.completedDate
                }
            } else {
                null
            }

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

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            repository.deleteBook(book)
            fetchMyBooks()
        }
    }
}