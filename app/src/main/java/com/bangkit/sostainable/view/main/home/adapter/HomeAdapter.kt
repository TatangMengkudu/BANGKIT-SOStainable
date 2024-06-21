package com.bangkit.sostainable.view.main.home.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.sostainable.data.remote.response.event.DataItem
import com.bangkit.sostainable.databinding.ItemEventBinding
import com.bangkit.sostainable.view.main.detail.DetailActivity
import com.bumptech.glide.Glide

class HomeAdapter : PagingDataAdapter<DataItem, HomeAdapter.HomeViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event!!)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(event)
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.DATA_EVENT, event.idEvent)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    class HomeViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: DataItem) {
            binding.apply {
                event.fotoLokasi?.let { url ->
                    Glide.with(root.context)
                        .load(url)
                        .into(ivEvent)
                }
                tvTitleEvent.text = event.judulEvent
                tvDescEvent.text = event.deskripsi
            }
        }
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: DataItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(
                oldItem: DataItem,
                newItem: DataItem
            ): Boolean {
                return oldItem.idEvent == newItem.idEvent
            }

            override fun areContentsTheSame(
                oldItem: DataItem,
                newItem: DataItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}