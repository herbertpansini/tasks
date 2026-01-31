package com.ztec.tarefas.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.ztec.tarefas.databinding.RowBalanceListBinding
import com.ztec.tarefas.service.model.TaskModel
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.Locale

class BalanceViewHolder(private val itemBinding: RowBalanceListBinding): RecyclerView.ViewHolder(itemBinding.root) {
    fun bindData(task: TaskModel) {
        itemBinding.textEmployee.text = task.userName
        itemBinding.textBalance.text = task.value.toString().toBigDecimal().formataParaBr()
    }

    fun BigDecimal.formataParaBr() : String {
        val moeda = DecimalFormat.getCurrencyInstance(Locale("pt", "BR"))
        return moeda.format(this).replace(moeda.currency.currencyCode, moeda.currency.currencyCode + " ")
    }
}