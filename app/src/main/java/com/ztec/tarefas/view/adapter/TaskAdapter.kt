package com.ztec.tarefas.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ztec.tarefas.databinding.RowTaskListBinding
import com.ztec.tarefas.service.listener.TaskListener
import com.ztec.tarefas.service.model.TaskModel
import com.ztec.tarefas.view.viewholder.TaskViewHolder

class TaskAdapter: RecyclerView.Adapter<TaskViewHolder>() {

    private var listTasks: List<TaskModel> = arrayListOf()
    private lateinit var listener: TaskListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = RowTaskListBinding.inflate(inflater, parent, false)
        return TaskViewHolder(itemBinding, listener)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bindData(listTasks[position])
    }

    override fun getItemCount(): Int {
        return listTasks.count()
    }

    fun updateTasks(list: List<TaskModel>) {
        listTasks = list
        notifyDataSetChanged()
    }

    fun attachListener(taskListener: TaskListener) {
        listener = taskListener
    }

}