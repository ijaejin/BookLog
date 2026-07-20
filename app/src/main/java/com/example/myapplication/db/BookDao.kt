package com.example.myapplication.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import com.example.myapplication.data.SavedBook

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: SavedBook)

    @Query("SELECT * FROM saved_books ORDER BY title ASC")
    suspend fun getAllBooks(): List<SavedBook>

    @Delete
    suspend fun deleteBook(book: SavedBook)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_books WHERE isbn = :isbn)")
    suspend fun isBookSaved(isbn: String): Boolean
}