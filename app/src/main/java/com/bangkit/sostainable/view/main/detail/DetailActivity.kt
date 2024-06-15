package com.bangkit.sostainable.view.main.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.sostainable.R
import com.bangkit.sostainable.data.factory.EventModelFactory
import com.bangkit.sostainable.data.json.JoinJson
import com.bangkit.sostainable.data.utils.Result
import com.bangkit.sostainable.databinding.ActivityDetailBinding
import com.bangkit.sostainable.view.main.MainActivity
import com.bangkit.sostainable.view.main.detail.adapter.ImageCarouselAdapter
import com.bangkit.sostainable.view.main.donate.DonateActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var bundle: Bundle
    private var id_event: String? = null

    private val detailViewModel: DetailViewModel by viewModels {
        EventModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            getDetail()
        }

        binding.icBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        bundle = Bundle()

        binding.donateButton.setOnClickListener {
            donateEvent()
        }

        binding.joinVolunteerButton.setOnClickListener {
            lifecycleScope.launch {
                val joinJson = JoinJson(id_event.toString())
                joinEvent(joinJson)
            }
        }
    }

    private suspend fun getDetail(){
        id_event = intent.getStringExtra(DATA_EVENT)
        detailViewModel.getDetailEvent(id_event.toString()).observe(this, Observer{ result ->
            when(result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.tvTitle.text = result.data.data?.judulEvent
                    binding.startEvent.text = result.data.data?.tanggalMulai
                    binding.endEvent.text = result.data.data?.tanggalSelesai
                    binding.clockStartEvent.text = result.data.data?.jamMulai
                    binding.clockEndEvent.text = result.data.data?.jamSelesai
                    binding.locationEvent.text = result.data.data?.alamat
                    binding.detailDescription.text = result.data.data?.deskripsiEvent
                    Glide.with(binding.imageView.context)
                        .load(result.data.data?.fotoLokasi?.get(1))
                        .into(binding.imageView)
                    val list = result.data.data?.fotoLokasi
                    getImageUrl(list!!)
                    binding.progressBar.visibility = View.GONE
                    bundle.putString("id_event", result.data.data.idEvent)
                    bundle.putString("title", result.data.data.judulEvent)
                    bundle.putString("imageUrl", result.data.data.fotoLokasi.get(1))
                    bundle.putString("description", result.data.data.deskripsiEvent)

                }
                is Result.Error -> {
                    showMessage(result.error)
                    Log.e("DETAIL", "getDetail: ${result.error}")
                    binding.progressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun getImageUrl(imageList: List<String?>){
        val recyclerView = findViewById<RecyclerView>(R.id.imageCarousel)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerView.adapter = ImageCarouselAdapter(imageList)
    }

    // TODO: Donate Event
    private fun donateEvent() {
        val intent = Intent(this, DonateActivity::class.java)
        intent.putExtra(DonateActivity.EXTRA_DONATION, bundle)
        startActivity(intent)
    }

    // TODO: Join Event
    private suspend fun joinEvent(joinJson: JoinJson) {
        detailViewModel.joinEvent(joinJson).observe(this,Observer{
            result -> when(result){
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    showMessage(result.data.message.toString())
                    binding.progressBar.visibility = View.GONE
                }
                is Result.Error -> {
                    showMessage(result.error)
                    binding.progressBar.visibility = View.GONE
                }
        }
        })
    }

    companion object {
        const val DATA_EVENT = "data_event"
    }
}