package com.bangkit.sostainable.view.main.donate.historyDonate.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.sostainable.data.local.room.entities.HistoryDonateEvent
import com.bangkit.sostainable.databinding.ItemHistoryDonateBinding
import com.bangkit.sostainable.view.utils.dateFormat
import com.bumptech.glide.Glide

class HistoryDonateAdapter:
    ListAdapter<HistoryDonateEvent, HistoryDonateAdapter.HistoryDonateEventViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryDonateEventViewHolder {
        val binding = ItemHistoryDonateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryDonateEventViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HistoryDonateEventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    class HistoryDonateEventViewHolder(
        private val binding: ItemHistoryDonateBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(event: HistoryDonateEvent) {
            binding.apply {
                event.imageUrl.let { url ->
                    Glide.with(root.context)
                        .load(url)
                        .into(ivEvent)
                }
                tvTitleEvent.text = event.title
                tvStartDate.text = event.Date
                tvPayment.text = event.payment
                tvNominal.text = event.nominal
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryDonateEvent>() {
            override fun areItemsTheSame(
                oldItem: HistoryDonateEvent,
                newItem: HistoryDonateEvent
            ): Boolean {
                return oldItem.idEvent == newItem.idEvent
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: HistoryDonateEvent,
                newItem: HistoryDonateEvent
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}