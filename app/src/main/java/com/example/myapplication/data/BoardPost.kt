package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "board_posts")
data class BoardPost(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val boardType: String,  // "REVIEW" 또는 "SHARE"
    val title: String,
    val content: String,
    val bookTitle: String = "",

    // 감상나눔 전용
    val rating: Float? = null,

    // 책나눔 전용
    val exchangeStatus: String = "",

    val createdAt: Long = System.currentTimeMillis()
)