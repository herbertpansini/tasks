package com.ztec.tasks.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ztec.tasks.R
import com.ztec.tasks.databinding.ActivityTaskFormBinding
import com.ztec.tasks.service.constants.TaskConstants
import com.ztec.tasks.service.model.CompanyModel
import com.ztec.tasks.service.model.TaskModel
import com.ztec.tasks.service.model.UserModel
import com.ztec.tasks.service.repository.SecurityPreferences
import com.ztec.tasks.viewmodel.TaskFormViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar

class TaskFormActivity: AppCompatActivity(),
                        View.OnClickListener,
                        DatePickerDialog.OnDateSetListener,
                        TimePickerDialog.OnTimeSetListener {

    private lateinit var viewModel: TaskFormViewModel
    private lateinit var binding: ActivityTaskFormBinding
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    private val hourFormat = SimpleDateFormat("HH:mm")
    private var companyId: Int? = null
    private var listCompany: List<CompanyModel> = mutableListOf()
    private var userId: Int? = null
    private var listUser: List<UserModel> = mutableListOf()
    private var taskIdentification = 0
    private val securityPreferences by lazy { SecurityPreferences(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[TaskFormViewModel::class.java]
        binding = ActivityTaskFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textDelete.setOnClickListener(this)
        binding.buttonDate.setOnClickListener(this)
        binding.buttonHour.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch(IO) {
            loadDataFromActivity()
            viewModel.loadCompanies()
            viewModel.loadUsers()
            withContext(Main) {
                observe()
            }
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_date) {
            handleDate()
        } else if (v.id == R.id.button_hour) {
            handleHour()
        } else if (v.id == R.id.text_delete) {
            if ("ROLE_ADMIN" == securityPreferences.get(TaskConstants.USER.ROLE)) {
                handleDelete()
            } else {
                toast("Acesso negado")
            }
        }
    }

    private fun loadDataFromActivity() {
        val bundle = intent.extras
        if (bundle != null) {
            taskIdentification = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            viewModel.load(taskIdentification)
        }
        binding.textDelete.visibility = if (taskIdentification > 0) View.VISIBLE else View.INVISIBLE
    }

    private fun observe() {
        viewModel.companyList.observe(this) {
            listCompany = it
            val list = mutableListOf<String>()
            for (p in it) {
                list.add(p.name)
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
            binding.spinnerCompany.adapter = adapter

            companyId?.let {
                binding.spinnerCompany.setSelection(getIndexCompany(it))
            }
        }

        viewModel.userList.observe(this) {
            listUser = it
            val list = mutableListOf<String>()
            for (p in it) {
                list.add(p.name)
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
            binding.spinnerUser.adapter = adapter

            userId?.let {
                binding.spinnerUser.setSelection(getIndexUser(it))
            }
        }

        viewModel.taskSave.observe(this) {
            if (it.status()) {
                if (taskIdentification == 0) {
                    toast(getString(R.string.task_created))
                } else {
                    toast(getString(R.string.task_updated))
                }
                finish()
            } else {
                toast(it.message())
            }
        }

        viewModel.task.observe(this) {
            companyId = it.companyId
            userId = it.userId

            if (listCompany.isNotEmpty()) {
                binding.spinnerCompany.setSelection(getIndexCompany(it.companyId))
            }

            if (listUser.isNotEmpty()) {
                binding.spinnerUser.setSelection(getIndexUser(it.userId))
            }

            val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(it.scheduledDatetime)
            binding.buttonDate.text = SimpleDateFormat("dd/MM/yyyy").format(date)
            binding.buttonHour.text = SimpleDateFormat("HH:mm").format(date)

            binding.editDescription.setText(it.description)
            binding.editValue.setText(it.value.toString().replace('.', ','))
            binding.editComment.setText(it.comment)

            this.enableOrDisableFields()
        }

        viewModel.taskLoad.observe(this) {
            if (!it.status()) {
                toast(it.message())
                finish()
            }
        }

        viewModel.delete.observe(this) {
            if (!it.status()) {
                Toast.makeText(this, it.message(), Toast.LENGTH_SHORT).show()
            } else {
                finish()
            }
        }
    }

    private fun enableOrDisableFields() {
        val isAdmin = ("ROLE_ADMIN" == securityPreferences.get(TaskConstants.USER.ROLE))
        binding.spinnerCompany.isEnabled = isAdmin
        binding.spinnerUser.isEnabled = isAdmin
        binding.buttonDate.isEnabled = isAdmin
        binding.buttonHour.isEnabled = isAdmin
        binding.editDescription.isEnabled = isAdmin
        binding.editValue.isEnabled = isAdmin
    }

    private fun getIndexCompany(companyId: Int): Int {
        var index = 0
        for (l in listCompany) {
            if (l.id == companyId) {
                break
            }
            index++
        }
        return index
    }

    private fun getIndexUser(userId: Int): Int {
        var index = 0
        for (l in listUser) {
            if (l.id == userId) {
                break
            }
            index++
        }
        return index
    }

    private fun toast(str: String) {
        Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
    }

    private fun handleSave() {
        val task = TaskModel().apply {
            this.id = taskIdentification
            this.companyId = listCompany[binding.spinnerCompany.selectedItemPosition].id
            this.userId = listUser[binding.spinnerUser.selectedItemPosition].id
            val date = SimpleDateFormat("dd/MM/yyyy HH:mm").parse("${binding.buttonDate.text} ${binding.buttonHour.text}")
            this.scheduledDatetime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
            this.description = binding.editDescription.text.toString()
            this.value = binding.editValue.text.toString().replace(',', '.').toDouble()
            this.comment = binding.editComment.text.toString()
        }
        viewModel.save(task)
    }

    private fun handleDelete() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirme!")
        builder.setMessage("Você tem certeza que deseja excluir essa tarefa?")
        builder.setPositiveButton(R.string.ok) { dialog, which ->
            val bundle = intent.extras
            if (bundle != null) {
                taskIdentification = bundle.getInt(TaskConstants.BUNDLE.TASKID)
                viewModel.delete(taskIdentification)
            }
        }
        builder.setNegativeButton(R.string.cancelar) { dialog, which -> }
        builder.show()
    }

    private fun handleDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this,this, year, month, day).show()
    }

    override fun onDateSet(v: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val dueDate = dateFormat.format(calendar.time)
        binding.buttonDate.text = dueDate
    }

    private fun handleHour() {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(this,this, hourOfDay, minute, true).show()
    }

    override fun onTimeSet(v: TimePicker, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val dueHour = hourFormat.format(calendar.time)
        binding.buttonHour.text = dueHour
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.register, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
//                if ("ROLE_ADMIN" == securityPreferences.get(TaskConstants.USER.ROLE)) {
                    handleSave()
//                } else {
//                    toast("Acesso negado")
//                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}