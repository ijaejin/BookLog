package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey
    val isbn: String,
    val title: String,
    val author: String,
    val publisher: String,
    val coverUrl: String,
    val contents: String,
    val price: Int,
    var readingStatus: String = "읽을 예정",
    var rating: Float? = null,
    var memo: String = "",
    var startDate: Long = System.currentTimeMillis(),
    var completedDate: Long? = null,
    var isAvailableForExchange: Boolean = false
)