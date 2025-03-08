package com.ftg2024.ftgcylinderapp.distributionmanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ftg2024.ftgcylinderapp.R
import com.ftg2024.ftgcylinderapp.distributionmanagement.model.AgentData

class AgentListAdapter(val dataList : List<AgentData>, val listener : OnAgentCLickListener) :
    RecyclerView.Adapter<AgentListAdapter.AgentListViewholder>() {
    class AgentListViewholder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val agentNameTextView = itemView.findViewById<TextView>(R.id.textview_item_agent_name)
        val agentMobileNo = itemView.findViewById<TextView>(R.id.textview_item_agent_no)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentListViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_agentlist_layout, parent, false)
        return AgentListViewholder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: AgentListViewholder, position: Int) {
        val model = dataList[position]
        holder.agentNameTextView.text = model.NAME
        holder.agentMobileNo.text = model.MOBILE_NO
        holder.itemView.setOnClickListener {
            listener.onAgentCLicked(model)
        }
    }

    interface OnAgentCLickListener {
        fun onAgentCLicked(model : AgentData)
    }
}