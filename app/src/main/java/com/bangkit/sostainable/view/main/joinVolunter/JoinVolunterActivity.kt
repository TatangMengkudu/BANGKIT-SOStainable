package com.bangkit.sostainable.view.main.joinVolunter

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.sostainable.data.factory.JoinEventFactory
import com.bangkit.sostainable.data.local.room.entities.JoinEvent
import com.bangkit.sostainable.databinding.ActivityJoinVolunterBinding
import com.bangkit.sostainable.view.main.MainActivity
import com.bangkit.sostainable.view.main.joinVolunter.adapter.JoinEventAdapter

class JoinVolunterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJoinVolunterBinding
    private val joinEvent by viewModels<JoinEventViewModel> {
        JoinEventFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinVolunterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }
    private fun setupView() {
        joinEvent.getAllJoinEvent().observe(this) { event ->
            showEventsData(event)
        }
        val layoutManager = LinearLayoutManager(this)
        binding.rvJoinEvent.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvJoinEvent.addItemDecoration(itemDecoration)

        moveBack()
    }

    private fun showEventsData (event: List<JoinEvent>) {
        val adapter = JoinEventAdapter(joinEvent)
        adapter.submitList(event)
        binding.rvJoinEvent.adapter = adapter
        adapter.setOnItemClickCallback(object : JoinEventAdapter.OnItemClickCallback {
            override fun onItemClicked(data: JoinEvent) {
                showSelectedEvent(data)
            }
        })
    }

    private fun showSelectedEvent(event: JoinEvent){
        Toast.makeText(this, event.title, Toast.LENGTH_SHORT).show()
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