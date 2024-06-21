package com.bangkit.sostainable.view.main.donate.historyDonate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.sostainable.data.factory.HistoryDonateFactory
import com.bangkit.sostainable.data.local.room.entities.HistoryDonateEvent
import com.bangkit.sostainable.databinding.ActivityHistoryDonateBinding
import com.bangkit.sostainable.view.main.MainActivity
import com.bangkit.sostainable.view.main.donate.historyDonate.adapter.HistoryDonateAdapter

class HistoryDonateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryDonateBinding
    private val historyDonate by viewModels<HistoryDonateViewModel> {
        HistoryDonateFactory.getInstance(application)
    }
    private lateinit var adapter: HistoryDonateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDonateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        observeViewModel()
    }

    private fun setupView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvHistoryDonateEvent.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvHistoryDonateEvent.addItemDecoration(itemDecoration)

        adapter = HistoryDonateAdapter()
        binding.rvHistoryDonateEvent.adapter = adapter

        moveBack()
    }

    private fun observeViewModel() {
        historyDonate.getAllHistoryDonate().observe(this) { event ->
            event?.let {
                if (it.isNotEmpty()) {
                    showEventsData(it)
                } else {
                    Log.d("HistoryDonateActivity", "No events received")
                }
            } ?: run {
                Log.d("HistoryDonateActivity", "Event list is null")
            }
        }
    }

    private fun showEventsData(event: List<HistoryDonateEvent>) {
        Log.d("HistoryDonateActivity", "Received ${event.size} events")
        if (event.isNotEmpty()) {
            adapter.submitList(event)
        } else {
            Log.d("HistoryDonateActivity", "Event list is empty")
        }
    }

    private fun moveBack() {
        binding.icBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}