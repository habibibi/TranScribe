package com.ikp.transcribe.ui.scan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ikp.transcribe.R
import com.ikp.transcribe.data.model.Item


class ItemListAdapter(val data: List<Item>) : RecyclerView.Adapter<ItemListAdapter.ViewHolder>() {
    private lateinit var context : Context
    class ViewHolder(row : View) : RecyclerView.ViewHolder(row){
        val itemName = row.findViewById<TextView>(R.id.itemName_value)
        val itemQuantity = row.findViewById<TextView>(R.id.itemQuantity_value)
        val itemPrice = row.findViewById<TextView>(R.id.itemPrice_value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.bill_item_view,
            parent, false)
        val holder = ViewHolder(layout)
        context = parent.context
        return holder
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemName.text = data[position].name
        holder.itemQuantity.text = data[position].quantity.toString()
        holder.itemPrice.text = context.getString(R.string.nominal,data[position].price)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

