package com.example.myapplication.ui.board

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.data.BoardPost
import com.example.myapplication.db.AppDatabase
import kotlinx.coroutines.launch

class BoardWriteActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etBookTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var etRating: EditText
    private lateinit var etExchangeStatus: EditText
    private lateinit var btnSave: Button

    private var boardType = "REVIEW"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_write)

        boardType = intent.getStringExtra("boardType") ?: "REVIEW"

        etTitle = findViewById(R.id.etTitle)
        etBookTitle = findViewById(R.id.etBookTitle)
        etContent = findViewById(R.id.etContent)
        etRating = findViewById(R.id.etRating)
        etExchangeStatus = findViewById(R.id.etExchangeStatus)
        btnSave = findViewById(R.id.btnSave)

        if (boardType == "REVIEW") {
            etRating.visibility = View.VISIBLE
            etExchangeStatus.visibility = View.GONE
        } else {
            etRating.visibility = View.GONE
            etExchangeStatus.visibility = View.VISIBLE
        }

        btnSave.setOnClickListener {

            val title = etTitle.text.toString().trim()
            val bookTitle = etBookTitle.text.toString().trim()
            val content = etContent.text.toString().trim()
            val ratingText = etRating.text.toString().trim()
            val exchangeStatus = etExchangeStatus.text.toString().trim()

            if (title.isBlank()) {
                etTitle.error = "제목을 입력하세요."
                etTitle.requestFocus()
                return@setOnClickListener
            }

            if (bookTitle.isBlank()) {
                etBookTitle.error = "책 제목을 입력하세요."
                etBookTitle.requestFocus()
                return@setOnClickListener
            }

            if (content.isBlank()) {
                etContent.error = "내용을 입력하세요."
                etContent.requestFocus()
                return@setOnClickListener
            }

            val rating = if (boardType == "REVIEW") {
                if (ratingText.isBlank()) {
                    null
                } else {
                    val parsedRating = ratingText.toFloatOrNull()

                    if (parsedRating == null || parsedRating !in 0f..5f) {
                        etRating.error = "평점은 0점부터 5점 사이로 입력하세요."
                        etRating.requestFocus()
                        return@setOnClickListener
                    }

                    parsedRating
                }
            } else {
                null
            }

            val post = BoardPost(
                boardType = boardType,
                title = title,
                content = content,
                bookTitle = bookTitle,
                rating = rating,
                exchangeStatus = if (boardType == "EXCHANGE") {
                    exchangeStatus
                } else {
                    ""
                }
            )

            btnSave.isEnabled = false

            lifecycleScope.launch {
                try {
                    AppDatabase
                        .getDatabase(this@BoardWriteActivity)
                        .boardDao()
                        .insertPost(post)

                    Toast.makeText(
                        this@BoardWriteActivity,
                        "게시글이 등록되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                } catch (e: Exception) {
                    btnSave.isEnabled = true

                    Toast.makeText(
                        this@BoardWriteActivity,
                        "게시글 등록에 실패했습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
