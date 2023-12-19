package com.ztec.tasks.view

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ztec.tasks.R
import com.ztec.tasks.databinding.ActivityBalanceBinding
import com.ztec.tasks.view.adapter.BalanceAdapter
import com.ztec.tasks.viewmodel.BalanceViewModel
import java.util.Calendar

class BalanceActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var viewModel: BalanceViewModel
    private lateinit var binding: ActivityBalanceBinding
    private val adapter = BalanceAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[BalanceViewModel::class.java]
        binding = ActivityBalanceBinding.inflate(layoutInflater)

        binding.recyclerAllBalances.layoutManager = LinearLayoutManager(this)
        binding.recyclerAllBalances.adapter = adapter

        var adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, this.getMonths())
        binding.spinnerMonth.adapter = adapter
        binding.spinnerMonth.setSelection(Calendar.getInstance().get(Calendar.MONTH))

        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getYears())
        binding.spinnerYear.adapter = adapter
        binding.spinnerYear.setSelection(getIndex(Calendar.getInstance().get(Calendar.YEAR).toString()))

        binding.buttonFilter.setOnClickListener(this)

        loadDataBalanceActivity()

        observe()

        setContentView(binding.root)
    }

    private fun getIndex(year: String): Int {
        var index = 0
        for (y in getYears()) {
            if (y == year) {
                break
            }
            index++
        }
        return index
    }

    private fun loadDataBalanceActivity() {
        balance()
    }

    private fun observe() {
        viewModel.balance.observe(this) {
            adapter.updateTasks(it)
        }
    }

    private fun getYears() = arrayListOf(Calendar.getInstance().get(Calendar.YEAR).toString(),
                                        (Calendar.getInstance().get(Calendar.YEAR) -1).toString())

    private fun getMonths() = arrayListOf("Janeiro",
                                            "Fevereiro",
                                            "Março",
                                            "Abril",
                                            "Maio",
                                            "Junho",
                                            "Julho",
                                            "Agosto",
                                            "Setembro",
                                            "Outubro",
                                            "Novembro",
                                            "Dezembro")

    override fun onClick(v: View) {
        if (v.id == R.id.button_filter) {
            balance()
        }
    }

    private fun balance() {
        val m = binding.spinnerMonth.selectedItemPosition
        val y = binding.spinnerYear.selectedItem.toString().toInt()
        val startScheduled = Calendar.getInstance()
        startScheduled.set(y, m, 1)

        val endScheduled = Calendar.getInstance()
        endScheduled.set(y, m, 1)
        val lastDay = endScheduled.getActualMaximum(Calendar.DAY_OF_MONTH)
        endScheduled.set(y, m, lastDay)
        viewModel.balance(startScheduled.time, endScheduled.time)
    }
}