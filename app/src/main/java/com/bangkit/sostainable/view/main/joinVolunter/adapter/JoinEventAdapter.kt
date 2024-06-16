package com.bangkit.sostainable.view.main.joinVolunter.adapter

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.sostainable.data.local.room.entities.JoinEvent
import com.bangkit.sostainable.databinding.ItemJoinVolunteerBinding
import com.bangkit.sostainable.view.main.detail.DetailActivity
import com.bangkit.sostainable.view.main.joinVolunter.JoinEventViewModel
import com.bangkit.sostainable.view.utils.dateFormat
import com.bumptech.glide.Glide

class JoinEventAdapter(
    private val joinEventViewModel: JoinEventViewModel
) : ListAdapter<JoinEvent, JoinEventAdapter.JoinEventViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class JoinEventViewHolder(
        private val binding: ItemJoinVolunteerBinding,
        private val joinEventViewModel: JoinEventViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(event: JoinEvent) {
            binding.apply {
                event.imageUrl.let { url ->
                    Glide.with(root.context)
                        .load(url)
                        .into(ivEvent)
                }
                tvTitleEvent.text = event.title
                tvStartDate.text = dateFormat(event.startDate)
                tvEndDate.text = dateFormat(event.endDate)
                tvAlamat.text = event.address
                btnCacelJoin.setOnClickListener {
                    deleteJoinEvent(event)
                }
            }
        }
        private fun deleteJoinEvent(event: JoinEvent){
            joinEventViewModel.deleteJoin(event)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JoinEventViewHolder {
        val binding = ItemJoinVolunteerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JoinEventViewHolder(binding, joinEventViewModel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: JoinEventViewHolder, position: Int) {
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
        fun onItemClicked(data: JoinEvent)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<JoinEvent>() {
            override fun areItemsTheSame(
                oldItem: JoinEvent,
                newItem: JoinEvent
            ): Boolean {
                return oldItem.idEvent == newItem.idEvent
            }

            override fun areContentsTheSame(
                oldItem: JoinEvent,
                newItem: JoinEvent
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}