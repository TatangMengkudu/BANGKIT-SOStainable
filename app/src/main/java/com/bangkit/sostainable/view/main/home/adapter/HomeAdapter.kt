package com.bangkit.sostainable.view.main.home.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.sostainable.data.remote.response.event.DataItem
import com.bangkit.sostainable.databinding.ItemEventBinding
import com.bangkit.sostainable.view.main.detail.DetailActivity
import com.bumptech.glide.Glide

class HomeAdapter : ListAdapter<DataItem, HomeAdapter.HomeViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
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
                // Foto Lokasi
                event.fotoLokasi?.let { url ->
                    Glide.with(root.context)
                        .load(url)
                        .into(ivEvent)
                }
                /** Foto User
                event.fotoUser?.let { url ->
                    Glide.with(root.context)
                        .load(url)
                        .into(ivUser)
                }
                */
                tvTitleEvent.text = event.judulEvent
                tvDescEvent.text = event.deskripsiEvent
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