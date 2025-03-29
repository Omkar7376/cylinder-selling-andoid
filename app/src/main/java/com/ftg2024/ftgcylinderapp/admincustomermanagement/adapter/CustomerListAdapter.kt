package com.ftg2024.ftgcylinderapp.admincustomermanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ftg2024.ftgcylinderapp.R
import com.ftg2024.ftgcylinderapp.admincustomermanagement.model.CustomerListResponse

class CustomerListAdapter(private var dataList : List<CustomerListResponse>, private val listener : OnCustomerClickListener) :
    RecyclerView.Adapter<CustomerListAdapter.CustomerListViewholder>(){
    class CustomerListViewholder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val customerNameTextView = itemView.findViewById<TextView>(R.id.textview_customer_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerListViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_customer_search,parent,false)
        return CustomerListViewholder(view)
    }

    override fun onBindViewHolder(holder: CustomerListViewholder, position: Int) {
       val model = dataList[position]
        holder.customerNameTextView.text = model.NAME
        holder.itemView.setOnClickListener {
            listener.onCustomerCLicked(model)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateData(newList: List<CustomerListResponse>) {
        dataList = newList
        notifyDataSetChanged()
    }

    interface OnCustomerClickListener {
        fun onCustomerCLicked(model : CustomerListResponse)
    }
}