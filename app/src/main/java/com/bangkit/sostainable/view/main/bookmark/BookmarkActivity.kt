package com.bangkit.sostainable.view.main.bookmark

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.sostainable.data.factory.BookmarkFactory
import com.bangkit.sostainable.data.local.room.entities.Bookmark
import com.bangkit.sostainable.databinding.ActivityBookmarkBinding
import com.bangkit.sostainable.view.main.MainActivity
import com.bangkit.sostainable.view.main.bookmark.adapter.BookmarkAdapter

class BookmarkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookmarkBinding
    private val bookmarkEvent by viewModels<BookmarkViewModel> {
        BookmarkFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        bookmarkEvent.getListBookmark().observe(this) { event ->
            showEventsData(event)
        }
        val layoutManager = LinearLayoutManager(this)
        binding.rvBookmark.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvBookmark.addItemDecoration(itemDecoration)

        moveBack()
    }

    private fun showEventsData (event: List<Bookmark>) {
        val adapter = BookmarkAdapter(bookmarkEvent)
        adapter.submitList(event)
        binding.rvBookmark.adapter = adapter
        adapter.setOnItemClickCallback(object : BookmarkAdapter.OnItemClickCallback {
            override fun onItemClicked(event: Bookmark) {
                showSelectedEvent(event)
            }
        })
    }

    private fun showSelectedEvent(event: Bookmark){
        Toast.makeText(this, event.judulEvent, Toast.LENGTH_SHORT).show()
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