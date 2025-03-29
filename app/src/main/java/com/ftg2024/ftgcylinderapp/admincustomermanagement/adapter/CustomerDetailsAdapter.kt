package com.ftg2024.ftgcylinderapp.admincustomermanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ftg2024.ftgcylinderapp.R
import com.ftg2024.ftgcylinderapp.admincustomermanagement.model.CustomerDetailsResponse
import com.ftg2024.ftgcylinderapp.admincustomermanagement.model.CustomerListResponse

class CustomerDetailsAdapter (private var data : List<CustomerDetailsResponse>) : RecyclerView.Adapter<CustomerDetailsAdapter.CustomerDetailsViewholder>(){
    class CustomerDetailsViewholder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val customerNameTextView = itemView.findViewById<TextView>(R.id.textview_activity_customer_management_name)
        val customerMoNoTextView = itemView.findViewById<TextView>(R.id.textview_activity_customer_management_mob_no)
        val customer142KgTextView = itemView.findViewById<TextView>(R.id.textview_activity_customer_management_14_2kg)
        val customer5KgdomTextView = itemView.findViewById<TextView>(R.id.textview_activity_customer_management_5kg_dom)
        val customer19KgTextView = itemView.findViewById<TextView>(R.id.textview_activity_customer_management_19kg)
        val customer5KgcomTextView = itemView.findViewById<TextView>(R.id.textview_activity_customer_management_5kg_com)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerDetailsViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_customer_details_table_layout,parent,false)
        return CustomerDetailsViewholder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CustomerDetailsViewholder, position: Int) {
        val model = data[position]
        holder.customerNameTextView.text = model.CUSTOMER_NAME
        holder.customerMoNoTextView.text = model.MOBILE_NO
        for (cylinder in model.customer_data) {
            when (cylinder.ITEM_ID) {
                1 -> {
                    holder.customer19KgTextView.text = cylinder.STOCK.toString()
                }
                2 -> {
                    holder.customer142KgTextView.text = cylinder.STOCK.toString()
                }
                3 -> {
                    holder.customer5KgcomTextView.text = cylinder.STOCK.toString()
                }
                4 -> {
                    holder.customer5KgdomTextView.text = cylinder.STOCK.toString()
                }
            }
        }
    }

    fun showData(newList: List<CustomerDetailsResponse>) {
        data = newList
        notifyDataSetChanged()
    }
}