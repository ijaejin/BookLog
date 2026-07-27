package com.example.myapplication.ui.board

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.data.BoardPost
import com.example.myapplication.db.AppDatabase
import kotlinx.coroutines.launch

class BoardDetailActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvBookTitle: TextView
    private lateinit var tvContent: TextView
    private lateinit var tvExtra: TextView
    private lateinit var btnDelete: Button

    private var post: BoardPost? = null
    private var postId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        tvTitle = findViewById(R.id.tvTitle)
        tvBookTitle = findViewById(R.id.tvBookTitle)
        tvContent = findViewById(R.id.tvContent)
        tvExtra = findViewById(R.id.tvExtra)
        btnDelete = findViewById(R.id.btnDelete)

        postId = intent.getIntExtra("postId", -1)

        loadPost()

        btnDelete.setOnClickListener {
            deletePost()
        }
    }

    private fun loadPost() {
        lifecycleScope.launch {

            post = AppDatabase
                .getDatabase(this@BoardDetailActivity)
                .boardDao()
                .getPostById(postId)

            post?.let {

                tvTitle.text = it.title
                tvBookTitle.text = "도서명 : ${it.bookTitle}"
                tvContent.text = it.content

                tvExtra.text =
                    if (it.boardType == "REVIEW") {
                        "별점 : ${it.rating ?: 0f}"
                    } else {
                        "나눔상태 : ${it.exchangeStatus}"
                    }
            }
        }
    }

    private fun deletePost() {
        lifecycleScope.launch {

            post?.let {
                AppDatabase
                    .getDatabase(this@BoardDetailActivity)
                    .boardDao()
                    .deletePost(it)

                finish()
            }
        }
    }
}