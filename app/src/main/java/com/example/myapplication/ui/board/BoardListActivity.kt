package com.example.myapplication.ui.board

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.db.AppDatabase
import kotlinx.coroutines.launch

class BoardListActivity : AppCompatActivity() {

    private lateinit var recyclerBoard: RecyclerView
    private lateinit var btnReviewTab: Button
    private lateinit var btnShareTab: Button
    private lateinit var fabWrite: FloatingActionButton
    private lateinit var adapter: BoardAdapter

    private var currentBoardType = "REVIEW"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_list)

        recyclerBoard = findViewById(R.id.recyclerBoard)
        btnReviewTab = findViewById(R.id.btnReviewTab)
        btnShareTab = findViewById(R.id.btnShareTab)
        fabWrite = findViewById(R.id.fabWrite)

        adapter = BoardAdapter(emptyList()) { post ->
            val intent = Intent(this, BoardDetailActivity::class.java)
            intent.putExtra("postId", post.id)
            startActivity(intent)
        }

        recyclerBoard.layoutManager = LinearLayoutManager(this)
        recyclerBoard.adapter = adapter

        btnReviewTab.setOnClickListener {
            currentBoardType = "REVIEW"
            loadPosts()
        }

        btnShareTab.setOnClickListener {
            currentBoardType = "SHARE"
            loadPosts()
        }

        fabWrite.setOnClickListener {
            val intent = Intent(this, BoardWriteActivity::class.java)
            intent.putExtra("boardType", currentBoardType)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadPosts()
    }

    private fun loadPosts() {
        lifecycleScope.launch {
            val posts = AppDatabase
                .getDatabase(this@BoardListActivity)
                .boardDao()
                .getPostsByType(currentBoardType)

            adapter.updatePosts(posts)
        }
    }
}