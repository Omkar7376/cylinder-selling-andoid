package com.ftg2024.ftgcylinderapp.stockmanagement.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ftg2024.ftgcylinderapp.R
import com.ftg2024.ftgcylinderapp.stockmanagement.model.StockDetail

class StockConfirmationAdapter(private val dataList : List<StockDetail>) : RecyclerView.Adapter<StockConfirmationAdapter.StockConfirmationViewHolder>() {
    class StockConfirmationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvType = itemView.findViewById<TextView>(R.id.tv_item_confirmation_type)
        val tvQyt = itemView.findViewById<TextView>(R.id.tv_item_confirmation_qyt)
        val tvDef = itemView.findViewById<TextView>(R.id.tv_item_confirmation_def)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockConfirmationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stock_confirmation_layout,parent,false)
        return StockConfirmationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: StockConfirmationViewHolder, position: Int) {
        val model = dataList[position]
        holder.tvType.text = getCylinderType(model.ID)
        holder.tvQyt.text = model.Value.toString()
        holder.tvDef.text = model.Defective.toString()
    }

    private fun getCylinderType(id : Int) : String {
        return when(id) {
            1 -> {
                "19 Kg Com"
            }
            2 -> {
                "14.2 Kg"
            }
            3 -> {
                "5 kg"
            }
            else -> "19 Kg BCMG"
        }
    }
}