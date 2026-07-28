package com.example.myapplication.ui.board

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.db.AppDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class BoardListActivity : AppCompatActivity() {

    private lateinit var recyclerBoard: RecyclerView
    private lateinit var btnReviewTab: Button
    private lateinit var btnShareTab: Button
    private lateinit var fabWrite: FloatingActionButton
    private lateinit var fabBack: FloatingActionButton
    private lateinit var tvEmpty: TextView
    private lateinit var adapter: BoardAdapter

    private var currentBoardType = "REVIEW"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_list)

        recyclerBoard = findViewById(R.id.recyclerBoard)
        btnReviewTab = findViewById(R.id.btnReviewTab)
        btnShareTab = findViewById(R.id.btnShareTab)
        fabWrite = findViewById(R.id.fabWrite)
        fabBack = findViewById(R.id.fabBack)
        tvEmpty = findViewById(R.id.tvEmpty)

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

        fabBack.setOnClickListener {
            finish()
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

            if (posts.isEmpty()) {
                tvEmpty.visibility = View.VISIBLE
                recyclerBoard.visibility = View.GONE
            } else {
                tvEmpty.visibility = View.GONE
                recyclerBoard.visibility = View.VISIBLE
            }
        }
    }
}