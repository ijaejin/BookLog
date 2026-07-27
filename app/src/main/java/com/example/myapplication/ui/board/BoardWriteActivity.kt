package com.example.myapplication.ui.board

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

        // 게시판 종류에 따라 입력칸 표시
        if (boardType == "REVIEW") {
            etRating.visibility = EditText.VISIBLE
            etExchangeStatus.visibility = EditText.GONE
        } else {
            etRating.visibility = EditText.GONE
            etExchangeStatus.visibility = EditText.VISIBLE
        }

        btnSave.setOnClickListener {

            val post = BoardPost(
                boardType = boardType,
                title = etTitle.text.toString(),
                content = etContent.text.toString(),
                bookTitle = etBookTitle.text.toString(),
                rating = etRating.text.toString().toFloatOrNull(),
                exchangeStatus = etExchangeStatus.text.toString()
            )

            lifecycleScope.launch {
                AppDatabase
                    .getDatabase(this@BoardWriteActivity)
                    .boardDao()
                    .insertPost(post)

                finish()
            }
        }
    }
}
