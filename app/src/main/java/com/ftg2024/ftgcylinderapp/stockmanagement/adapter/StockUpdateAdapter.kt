package com.ftg2024.ftgcylinderapp.stockmanagement.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ftg2024.ftgcylinderapp.R
import com.ftg2024.ftgcylinderapp.stockmanagement.model.StockDetailsData

class StockUpdateAdapter(val dataList: List<StockDetailsData>, val isFilled : Boolean) : RecyclerView.Adapter<StockUpdateAdapter.StockViewHolder>() {
    class StockViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val textViewDate: TextView = itemView.findViewById<TextView>(R.id.textview_item_add_stock_date)
        val textviewCommercialCylinderCount: TextView = itemView.findViewById<TextView>(R.id.textview_item_add_stock_commercial_count)
        val textviewDomesticCylinderCount: TextView = itemView.findViewById<TextView>(R.id.textview_item_add_stock_domestic_count)
        val textviewMiniCylinderCount: TextView = itemView.findViewById<TextView>(R.id.textview_item_add_stock_mini_count)
        val labelInvoiceNumber: TextView = itemView.findViewById<TextView>(R.id.label_item_add_stock_details_number)
        val textviewInvoiceNumber: TextView = itemView.findViewById<TextView>(R.id.textview_item_add_stock_details_number)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        Log.d("####", "onCreateViewHolder: ")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_return_stock_details,parent,false)
        return StockViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val model = dataList[position]
        Log.d("###", "onBindViewHolder: $model")
        holder.labelInvoiceNumber.text = if (isFilled) {
            "Invoice Number"
        } else {
            "ERV Number"
        }
        holder.textViewDate.text = model.DATE
        holder.textviewCommercialCylinderCount.text = model.Commercial.toString()
        holder.textviewDomesticCylinderCount.text = model.Domestic.toString()
        holder.textviewMiniCylinderCount.text = model.Mini.toString()
        holder.textviewInvoiceNumber.text = model.INV_ERV_NO.toString()
    }
}