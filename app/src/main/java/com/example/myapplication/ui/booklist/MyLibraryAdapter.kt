package com.example.myapplication.ui.booklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.data.SavedBook
import com.example.myapplication.R


class MyLibraryAdapter(
    private val onItemClick: (SavedBook) -> Unit,
    private val onItemLongClick: (SavedBook) -> Unit
) : ListAdapter<SavedBook, MyLibraryAdapter.BookViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mylibrary_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyLibraryAdapter.BookViewHolder, position: Int) {
        // 책 데이터를 뷰홀더에 전달
        holder.bind(getItem(position))
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // xml 요소들 연결
        private val ivThumbnail: ImageView = itemView.findViewById<ImageView>(R.id.ivThumbnail)
        private val tvTitle: TextView = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvAuthor: TextView = itemView.findViewById<TextView>(R.id.tvAuthor)
        private val tvStatus: TextView = itemView.findViewById<TextView>(R.id.tvStatus)

        fun bind(book: SavedBook) {
            tvTitle.text = book.title
            tvAuthor.text = book.authors
            tvStatus.text = book.readingStatus

            // Glide로 인터넷 url에서 표지 불러오기
            Glide.with(itemView.context)
                .load(book.thumbnail)
                .placeholder(android.R.drawable.ic_menu_gallery) //로딩중일때 기본 아이콘
                .error(android.R.drawable.ic_menu_report_image) // 실패 기본 아이콘
                .into(ivThumbnail)

            //짧게 클릭 (상세 확인 & 상태 수정)
            itemView.setOnClickListener { onItemClick(book) }

            //길게 누를때 (삭제)
            itemView.setOnLongClickListener {
                onItemLongClick(book)
                true
            }
        }
    }

    // 데이터가 변경되었을 때 변경된 부분만 갱신
    class BookDiffCallback : DiffUtil.ItemCallback<SavedBook>() {
        override fun areItemsTheSame(oldItem: SavedBook, newItem: SavedBook): Boolean {
            return oldItem.isbn == newItem.isbn
        }

        override fun areContentsTheSame(oldItem: SavedBook, newItem: SavedBook): Boolean {
            return oldItem == newItem
        }
    }
}