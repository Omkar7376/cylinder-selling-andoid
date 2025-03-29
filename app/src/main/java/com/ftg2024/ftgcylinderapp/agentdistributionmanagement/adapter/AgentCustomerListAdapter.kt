package com.ftg2024.ftgcylinderapp.agentdistributionmanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ftg2024.ftgcylinderapp.R
import com.ftg2024.ftgcylinderapp.agentdistributionmanagement.model.AgentCustomerData

class AgentCustomerListAdapter(val dataList : List<AgentCustomerData>, val listener : OnAgentCustClickListener) :
    RecyclerView.Adapter<AgentCustomerListAdapter.AgentCustomerListViewholder>(){

    class AgentCustomerListViewholder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val customerNameTextView = itemView.findViewById<TextView>(R.id.textview_item_customer_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentCustomerListViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_agent_customer_list_layout, parent, false)
        return AgentCustomerListViewholder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: AgentCustomerListViewholder, position: Int) {
        val model = dataList[position]
        holder.customerNameTextView.text = model.NAME
        holder.itemView.setOnClickListener {
            listener.onAgentCustCLicked(model)
        }
    }

    interface OnAgentCustClickListener {
        fun onAgentCustCLicked(model : AgentCustomerData)
    }
}