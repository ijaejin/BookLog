package com.example.myapplication.data

// 카카오 도서 검색 API 전체 응답
data class BookResponse(
    val documents: List<Book>,
    val meta: Meta
)

// 검색 결과 도서 한 권
data class Book(
    val title: String,
    val contents: String,
    val authors: List<String>,
    val publisher: String,
    val thumbnail: String,
    val isbn: String,
    val price: Int,
    val datetime: String
)

// 페이지 정보
data class Meta(
    val is_end: Boolean,
    val pageable_count: Int,
    val total_count: Int
)