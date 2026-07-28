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
import com.example.myapplication.R
import com.example.myapplication.data.Book

class MyLibraryAdapter(
    private val onItemClick: (Book) -> Unit,
    private val onItemLongClick: (Book) -> Unit
) : ListAdapter<Book, MyLibraryAdapter.BookViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mylibrary_book, parent, false)

        return BookViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BookViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class BookViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val ivThumbnail: ImageView =
            itemView.findViewById(R.id.ivThumbnail)

        private val tvTitle: TextView =
            itemView.findViewById(R.id.tvTitle)

        private val tvAuthor: TextView =
            itemView.findViewById(R.id.tvAuthor)

        private val tvStatus: TextView =
            itemView.findViewById(R.id.tvStatus)

        fun bind(book: Book) {
            tvTitle.text = book.title
            tvAuthor.text = book.author
            tvStatus.text = book.readingStatus

            Glide.with(itemView.context)
                .load(book.coverUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(ivThumbnail)

            itemView.setOnClickListener {
                onItemClick(book)
            }

            itemView.setOnLongClickListener {
                onItemLongClick(book)
                true
            }
        }
    }

    class BookDiffCallback : DiffUtil.ItemCallback<Book>() {

        override fun areItemsTheSame(
            oldItem: Book,
            newItem: Book
        ): Boolean {
            return oldItem.isbn == newItem.isbn
        }

        override fun areContentsTheSame(
            oldItem: Book,
            newItem: Book
        ): Boolean {
            return oldItem == newItem
        }
    }
}