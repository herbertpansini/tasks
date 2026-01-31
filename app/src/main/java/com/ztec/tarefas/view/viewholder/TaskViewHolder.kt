package com.ztec.tarefas.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.ztec.tarefas.databinding.RowTaskListBinding
import com.ztec.tarefas.service.listener.TaskListener
import com.ztec.tarefas.service.model.TaskModel
import java.text.SimpleDateFormat

class TaskViewHolder(private val itemBinding: RowTaskListBinding,
                     private val listener: TaskListener): RecyclerView.ViewHolder(itemBinding.root) {
    fun bindData(task: TaskModel) {
        itemBinding.textCompany.text = "[${task.companyName}] - ${task.userName}"
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(task.scheduledDatetime)
        itemBinding.textHour.text = SimpleDateFormat("HH:mm").format(date)
        itemBinding.textDescription.text = task.description
        itemBinding.textComment.text = task.comment

        itemView.setOnClickListener { listener.onListClick(task.id) }
    }
}