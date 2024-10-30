package com.example.snapeditapp

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.snapeditapp.databinding.ItemEditFilterBinding
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter

class EditImageAdapter(
    private val filters: List<GPUImageFilter>,
    private val originalBitmap: Bitmap,
    private val onFilterClick: (GPUImageFilter) -> Unit
) : RecyclerView.Adapter<EditImageAdapter.FilterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binding = ItemEditFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val gpuImage = GPUImage(holder.itemView.context)
        gpuImage.setImage(originalBitmap)
        gpuImage.setFilter(filters[position])
        val filteredBitmap = gpuImage.bitmapWithFilterApplied
        holder.binding.imageFilterName.setImageBitmap(filteredBitmap)
        holder.binding.textFilterName.text = "Filter ${position + 1}"
    }

    override fun getItemCount() = filters.size

    inner class FilterViewHolder(val binding: ItemEditFilterBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                onFilterClick(filters[adapterPosition])
            }
        }
    }
}
