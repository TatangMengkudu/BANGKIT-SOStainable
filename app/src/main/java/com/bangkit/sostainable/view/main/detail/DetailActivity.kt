package com.bangkit.sostainable.view.main.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.sostainable.R
import com.bangkit.sostainable.databinding.ActivityDetailBinding
import com.bangkit.sostainable.view.main.MainActivity
import com.bangkit.sostainable.view.main.detail.adapter.ImageCarouselAdapter

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val imageList = listOf(
            R.drawable.iv_news,
            R.drawable.iv_news,
            R.drawable.iv_news
        )
        val recyclerView = findViewById<RecyclerView>(R.id.imageCarousel)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = ImageCarouselAdapter(imageList)

        binding.icBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}