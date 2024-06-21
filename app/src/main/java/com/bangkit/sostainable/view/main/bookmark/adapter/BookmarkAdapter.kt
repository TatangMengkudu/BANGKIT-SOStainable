package com.bangkit.sostainable.view.main.bookmark.adapter

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.sostainable.data.local.room.entities.Bookmark
import com.bangkit.sostainable.databinding.ItemBookmarkBinding
import com.bangkit.sostainable.view.main.bookmark.BookmarkViewModel
import com.bangkit.sostainable.view.main.detail.DetailActivity
import com.bangkit.sostainable.view.utils.dateFormat
import com.bumptech.glide.Glide

class BookmarkAdapter(
    private val bookmarkViewModel: BookmarkViewModel
) : ListAdapter<Bookmark, BookmarkAdapter.BookmarkViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class BookmarkViewHolder(
        private val binding: ItemBookmarkBinding,
        private val bookmarkViewModel: BookmarkViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(event: Bookmark) {
            binding.apply {
                event.fotoLokasi?.let { url ->
                    Glide.with(root.context)
                        .load(url)
                        .into(ivEvent)
                }
                tvTitleEvent.text = event.judulEvent
                tvStartDate.text = dateFormat(event.tanggalMulai!!)
                tvEndDate.text = dateFormat(event.tanggalSelesai!!)
                tvAlamat.text = event.alamat
                deleteBookmark.setOnClickListener {
                    deleteBookmark(event)
                }
            }
        }
        private fun deleteBookmark(event: Bookmark){
            bookmarkViewModel.deleteBookmark(event)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val binding = ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkViewHolder(binding, bookmarkViewModel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(event)
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.DATA_EVENT, event.idEvent)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Bookmark)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Bookmark>() {
            override fun areItemsTheSame(
                oldItem: Bookmark,
                newItem: Bookmark
            ): Boolean {
                return oldItem.idEvent == newItem.idEvent
            }

            override fun areContentsTheSame(
                oldItem: Bookmark,
                newItem: Bookmark
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}