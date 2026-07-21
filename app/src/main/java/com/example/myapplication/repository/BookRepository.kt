package com.example.myapplication.repository

import com.example.myapplication.data.SavedBook
import com.example.myapplication.db.BookDao
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao) {
    // 내 서재 전체 목록 가져오기
    suspend fun getAllBooks(): List<SavedBook> {
        return bookDao.getAllBooks()
    }

    // 책 저장
    suspend fun insertBook(book: SavedBook) {
        bookDao.insertBook(book)
    }

    // 책 정보 수정
    suspend fun updateBook(book: SavedBook){
        bookDao.updateBook(book)
    }

    //책 삭제
    suspend fun deleteBook(book: SavedBook){
        bookDao.deleteBook(book)
    }
}

// annotation class BookRepository
