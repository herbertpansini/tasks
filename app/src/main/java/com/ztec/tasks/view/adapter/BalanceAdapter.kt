package com.ztec.tasks.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ztec.tasks.databinding.RowBalanceListBinding
import com.ztec.tasks.service.model.TaskModel
import com.ztec.tasks.view.viewholder.BalanceViewHolder

class BalanceAdapter: RecyclerView.Adapter<BalanceViewHolder>() {

    private var listTasks: List<TaskModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = RowBalanceListBinding.inflate(inflater, parent, false)
        return BalanceViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BalanceViewHolder, position: Int) {
        holder.bindData(listTasks[position])
    }

    override fun getItemCount(): Int {
        return listTasks.count()
    }

    fun updateTasks(list: List<TaskModel>) {
        listTasks = list
        notifyDataSetChanged()
    }
}