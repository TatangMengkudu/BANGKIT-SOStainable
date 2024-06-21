package com.bangkit.sostainable.view.main.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.sostainable.databinding.ItemImageCarouselBinding
import com.bumptech.glide.Glide

class ImageCarouselAdapter(private val images: List<String?>): RecyclerView.Adapter<ImageCarouselAdapter.ImageViewHolder>() {

    class ImageViewHolder(private var binding: ItemImageCarouselBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: String) {
            Glide.with(binding.ivImage.context)
                .load(image)
                .into(binding.ivImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = ItemImageCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        images[position]?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return images.size
    }
}