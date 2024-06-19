package com.bangkit.sostainable.view.main.myEvent

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.sostainable.data.factory.EventModelFactory
import com.bangkit.sostainable.data.remote.response.event.ListEventItem
import com.bangkit.sostainable.data.utils.Result
import com.bangkit.sostainable.databinding.ActivityMyEventBinding
import com.bangkit.sostainable.view.main.myEvent.adapter.MyEventAdapter
import kotlinx.coroutines.launch

class MyEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyEventBinding
    private val myEventViewModel by viewModels<MyEventViewModel> {
        EventModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
    }

    private fun setupView(){
        val layoutManager = LinearLayoutManager(this)
        binding.rvMyEvent.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvMyEvent.addItemDecoration(itemDecoration)
        lifecycleScope.launch {
            getMyEvent()
        }
    }

    private suspend fun getMyEvent(){
        myEventViewModel.getMyEvent().observe(this, Observer {
                result -> when(result){
            is Result.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is Result.Success -> {
                showEventsData(result.data.listEvent)
                binding.progressBar.visibility = View.GONE
            }
            is Result.Error -> {
                val alertDialogBuilder = AlertDialog.Builder(this@MyEventActivity)
                alertDialogBuilder.apply {
                    setTitle("FAILED")
                    setMessage("FAILED to get data event, with error message: ${result.error}")
                    setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
                binding.progressBar.visibility = View.GONE
            }
        }
        })
    }

    private fun showEventsData (event: List<ListEventItem>) {
        val adapter = MyEventAdapter()
        adapter.submitList(event)
        binding.rvMyEvent.adapter = adapter
    }
}