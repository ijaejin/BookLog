package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.ui.SearchActivity
import com.example.myapplication.ui.board.BoardListActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        val btnSearch = findViewById<Button>(R.id.btnGoSearch)
        btnSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        val btnMyLibrary = findViewById<Button>(R.id.btnMyLibrary)
        btnMyLibrary.setOnClickListener {
            startActivity(Intent(this, MyLibraryActivity::class.java))
        }

        val btnBoard = findViewById<Button>(R.id.btnBoard)
        btnBoard.setOnClickListener {
            startActivity(Intent(this, BoardListActivity::class.java))
        }
    }
}