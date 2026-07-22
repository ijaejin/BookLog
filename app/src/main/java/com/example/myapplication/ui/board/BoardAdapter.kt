package com.example.myapplication.ui.board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.BoardPost

class BoardAdapter(
    private var posts: List<BoardPost>,
    private val onItemClick: (BoardPost) -> Unit
) : RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {

    class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPostType: TextView = itemView.findViewById(R.id.tvPostType)
        val tvPostTitle: TextView = itemView.findViewById(R.id.tvPostTitle)
        val tvPostBookTitle: TextView = itemView.findViewById(R.id.tvPostBookTitle)
        val tvPostContent: TextView = itemView.findViewById(R.id.tvPostContent)
        val tvPostExtra: TextView = itemView.findViewById(R.id.tvPostExtra)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_board_post, parent, false)

        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val post = posts[position]

        holder.tvPostType.text =
            if (post.boardType == "REVIEW") "감상나눔" else "책나눔"

        holder.tvPostTitle.text = post.title
        holder.tvPostBookTitle.text = "도서명: ${post.bookTitle}"
        holder.tvPostContent.text = post.content

        holder.tvPostExtra.text =
            if (post.boardType == "REVIEW") {
                "별점: ${post.rating ?: 0f}"
            } else {
                "상태: ${post.exchangeStatus}"
            }

        holder.itemView.setOnClickListener {
            onItemClick(post)
        }
    }

    override fun getItemCount(): Int = posts.size

    fun updatePosts(newPosts: List<BoardPost>) {
        posts = newPosts
        notifyDataSetChanged()
    }
}