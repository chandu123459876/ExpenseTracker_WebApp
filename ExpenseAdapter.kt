package com.softwareapp.expensestracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
class ExpenseAdapter(
    private var list: MutableList<ExpenseItem>,
    private val onDeleteClick: (ExpenseItem) -> Unit
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {
    data class ExpenseItem(val id: Int, val amount: Double, val reason: String, val time: Long, val type: String = "")
    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvReason: TextView = itemView.findViewById(R.id.tvReason)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }
    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val item = list[position]
        holder.tvReason.text = item.reason
        holder.tvAmount.text = holder.itemView.context.getString(R.string.amount_format, item.amount.toString())
        
        if (item.type == "expense") {
            holder.tvAmount.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
        } else {
            holder.tvAmount.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
        }
        val sdf = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
        holder.tvDate.text = sdf.format(Date(item.time))
        holder.ivDelete.setOnClickListener {
            onDeleteClick(item)
        }
    }
    override fun getItemCount(): Int = list.size
    fun updateData(newList: List<ExpenseItem>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}