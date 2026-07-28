package com.example.myapplication.repository

import com.example.myapplication.data.Book
import com.example.myapplication.db.BookDao

class BookRepository(private val bookDao: BookDao) {

    // 내 서재 전체 목록 가져오기
    suspend fun getAllBooks(): List<Book> {
        return bookDao.getAllBooks()
    }

    // 책 저장
    suspend fun insertBook(book: Book) {
        bookDao.insertBook(book)
    }

    // 책 정보 수정
    suspend fun updateBook(book: Book) {
        bookDao.updateBook(book)
    }

    // 책 삭제
    suspend fun deleteBook(book: Book) {
        bookDao.deleteBook(book)
    }
}
