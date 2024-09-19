package com.example.prakt22_2.Data

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.prakt22_2.R

class DataAdapter(
    private val dataList: List<DataEntity>,
    private val onItemLongClick: (DataEntity) -> Unit
) : RecyclerView.Adapter<DataAdapter.DataViewHolder>() {

    class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val inputTextView: TextView = view.findViewById(R.id.inputTextView)
        val dataTextView: TextView = view.findViewById(R.id.dataTextView)
        val imageView: ImageView = view.findViewById(R.id.sprite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.data_item, parent, false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val data = dataList[position]
        holder.inputTextView.text = data.name
        holder.dataTextView.text = data.abilities.joinToString(", ")

        data.imageBytes?.let { bytes ->
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            Glide.with(holder.itemView.context)
                .load(bitmap)
                .into(holder.imageView)
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClick(data)
            true
        }
    }

    override fun getItemCount(): Int = dataList.size
}
