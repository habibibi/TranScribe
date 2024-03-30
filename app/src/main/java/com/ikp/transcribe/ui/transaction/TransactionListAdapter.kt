package com.ikp.transcribe.ui.transaction

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ikp.transcribe.R
import com.ikp.transcribe.data.table.Transaction

class TransactionListAdapter() : ListAdapter<Transaction,TransactionListAdapter.ViewHolder>(TransactionDiffCallback()) {
    private lateinit var context : Context
    class ViewHolder(val row : View) : RecyclerView.ViewHolder(row){
        val transactionDate = row.findViewById<TextView>(R.id.transactionDate)
        val transactionName = row.findViewById<TextView>(R.id.transactionName)
        val transactionPrice = row.findViewById<TextView>(R.id.transactionPrice)
        val transactionCategory = row.findViewById<TextView>(R.id.transactionCategory)
        val transactionLocation = row.findViewById<TextView>(R.id.transactionLocation)
        val transactionCard = row.findViewById<View>(R.id.transactionCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_view,
            parent, false)
        val holder = ViewHolder(layout)
        context = parent.context
        return holder
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // TODO: add date
        holder.transactionDate.text = getItem(position).tanggal
        holder.transactionName.text = getItem(position).judul
        holder.transactionPrice.text = context.getString(R.string.nominal,getItem(position).nominal)
        holder.transactionCategory.text = getItem(position).kategori
        holder.transactionLocation.text = getItem(position).lokasi

        holder.transactionCard.setOnClickListener{
            val id = getItem(position).id
            val intent = Intent(context,AddTransactionActivity::class.java)
            intent.putExtra("id",id)
            context.startActivity(intent)
        }
    }
}

class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>(){
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return (oldItem.email == newItem.email &&
                oldItem.judul == newItem.judul &&
                oldItem.kategori == newItem.kategori &&
                oldItem.nominal == newItem.nominal &&
                oldItem.lokasi == newItem.lokasi)
    }
}
