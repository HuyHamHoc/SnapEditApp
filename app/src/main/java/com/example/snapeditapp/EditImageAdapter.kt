package com.example.snapeditapp

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter

class EditImageAdapter(
    private val filters: List<GPUImageFilter>,
    private val originalBitmap: Bitmap,
    private val onFilterClick: (GPUImageFilter) -> Unit
) : RecyclerView.Adapter<EditImageAdapter.FilterViewHolder>() {

    inner class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageFilterName)
        val filterName: TextView = itemView.findViewById(R.id.textFilterName)

        init {
            itemView.setOnClickListener {
                onFilterClick(filters[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_edit_filter, parent, false)
        return FilterViewHolder(view)
    }


    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val gpuImage = GPUImage(holder.itemView.context)
        gpuImage.setImage(originalBitmap)
        gpuImage.setFilter(filters[position])
        val filteredBitmap = gpuImage.bitmapWithFilterApplied
        holder.imageView.setImageBitmap(filteredBitmap)
        holder.filterName.text = "Filter ${position + 1}"
    }

    override fun getItemCount() = filters.size
}
