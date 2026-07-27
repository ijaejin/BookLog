package com.example.myapplication.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.BoardPost

@Dao
interface BoardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: BoardPost)

    @Query(
        "SELECT * FROM board_posts " +
                "WHERE boardType = :boardType " +
                "ORDER BY createdAt DESC"
    )
    suspend fun getPostsByType(boardType: String): List<BoardPost>

    @Query("SELECT * FROM board_posts WHERE id = :postId LIMIT 1")
    suspend fun getPostById(postId: Int): BoardPost?

    @Delete
    suspend fun deletePost(post: BoardPost)
}