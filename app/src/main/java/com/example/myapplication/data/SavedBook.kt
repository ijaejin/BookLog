package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_books")
data class SavedBook(
    @PrimaryKey
    val isbn: String,
    val title: String,
    val authors: String,
    val publisher: String,
    val thumbnail: String,
    val price: Int,
    val contents: String,

    var readingStatus: String = "읽기 전",
    var memo: String = "",                  // 감상 메모
    var completedDate: String = ""
)