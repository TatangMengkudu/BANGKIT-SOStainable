package com.bangkit.sostainable.view.main.myEvent.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.sostainable.R
import com.bangkit.sostainable.data.remote.response.event.ListEventItem
import com.bangkit.sostainable.databinding.ItemEventEndDateBinding
import com.bangkit.sostainable.view.main.createReport.CreateReportActivity
import com.bangkit.sostainable.view.utils.dateFormat
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MyEventAdapter: ListAdapter<ListEventItem, MyEventAdapter.MyEventViewHolder>(DIFF_CALLBACK)  {
    class MyEventViewHolder(
        private val binding: ItemEventEndDateBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(event: ListEventItem) {
            binding.apply {
                event.fotoLokasi?.let { urlArray ->
                    if (urlArray.isNotEmpty()) {
                        val url = urlArray[0]
                        Glide.with(root.context)
                            .load(url)
                            .into(ivEvent)
                    }
                }
                tvTitleEvent.text = event.judulEvent
                tvEndDate.text = dateFormat(event.tanggalSelesai.toString())
                tvAlamat.text = event.alamat
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val endDate: Date? = dateFormat.parse(event.tanggalSelesai.toString())
                val currentDate = Calendar.getInstance().time
                if (currentDate.after(endDate)) {
                    btnAddReport.visibility = View.VISIBLE
                } else {
                    btnAddReport.visibility = View.GONE
                }
                if (event.status == true){
                    btnAddReport.text = "Closed"
                    btnAddReport.isEnabled = false
                    btnAddReport.isClickable = false
                    btnAddReport.setTextColor(root.context.resources.getColor(R.color.black))
                    btnAddReport.setBackgroundColor(root.context.resources.getColor(R.color.grey))
                }else{
                    btnAddReport.text = "Add Report"
                }
                btnAddReport.setOnClickListener {
                    val intentDetail = Intent(binding.root.context, CreateReportActivity::class.java)
                    intentDetail.putExtra(CreateReportActivity.DATA_EVENT, event.idEvent)
                    binding.root.context.startActivity(intentDetail)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyEventViewHolder {
        val binding = ItemEventEndDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyEventViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyEventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventItem,
                newItem: ListEventItem
            ): Boolean {
                return oldItem.idEvent == newItem.idEvent
            }

            override fun areContentsTheSame(
                oldItem: ListEventItem,
                newItem: ListEventItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}