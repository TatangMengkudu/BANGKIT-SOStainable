package com.bangkit.sostainable.view.main.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.sostainable.databinding.ItemImageCarouselBinding

class ImageCarouselAdapter(private val images: List<Int>): RecyclerView.Adapter<ImageCarouselAdapter.ImageViewHolder>() {

    class ImageViewHolder(private var binding: ItemImageCarouselBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Int) {
            binding.ivImage.setImageResource(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = ItemImageCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

}