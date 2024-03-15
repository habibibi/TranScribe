package com.ikp.transcribe.ui.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ikp.transcribe.R

class TransactionListAdapter(val data:List<Int>) : RecyclerView.Adapter<TransactionListAdapter.ViewHolder>() {
    class ViewHolder(private val row : View) : RecyclerView.ViewHolder(row){
        val textView: TextView = row.findViewById<TextView>(R.id.number)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_view,
            parent, false)
        return ViewHolder(layout)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = data[position].toString()
    }

    override fun getItemCount(): Int = data.size


}