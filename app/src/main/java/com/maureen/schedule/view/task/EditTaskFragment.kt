package com.maureen.schedule.view.task

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.maureen.schedule.R
import com.maureen.schedule.adapter.StepAdapter
import com.maureen.schedule.database.Step
import com.maureen.schedule.database.Task
import com.maureen.schedule.databinding.FragmentEditTaskBinding
import com.maureen.schedule.entity.TaskWithStep
import com.maureen.schedule.utils.KEY_CHECKLIST_ID
import com.maureen.schedule.utils.KEY_TASK_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


class EditTaskFragment : Fragment() {

    companion object {
        private const val TAG = "EditTaskFragment"
    }

    private lateinit var viewBinding: FragmentEditTaskBinding
    private val viewModel: TaskViewModel by viewModels()
    private var checklistId: Long = 1
    private var taskId: Long = -1
    private var dataBean: TaskWithStep? = null
    private var adapter: StepAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentEditTaskBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {
        viewBinding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        adapter = StepAdapter(delAction = {
            dataBean?.steps?.remove(it)
        })
        viewBinding.rvStep.adapter = adapter
        viewBinding.edtStep.setOnEditorActionListener { v, actionId, _ ->
            Log.d(TAG, "initView: $actionId")
            if (v.text.isNotEmpty() && actionId == EditorInfo.IME_ACTION_NEXT) {
                val step = Step()
                step.name = v.text.toString()
                step.finishTime = System.currentTimeMillis()
                Log.d(TAG, "initView: ${dataBean?.steps?.size}")
                dataBean?.steps?.add(step)
                Log.d(TAG, "initView: add ${dataBean?.steps?.size}")
                adapter?.submitList(dataBean?.steps)
            }
            v.text = ""
            v.clearFocus()
            false
        }
        viewBinding.btnSave.setOnClickListener { saveTask() }
    }

    private fun initData() = lifecycleScope.launch {
        arguments?.let {
            checklistId = it.getLong(KEY_CHECKLIST_ID, 1)
            taskId = it.getLong(KEY_TASK_ID, -1)
        }
        if (-1L == taskId) {
            viewBinding.toolBar.title = getString(R.string.add_task)
            val task = Task()
            task.createTime = System.currentTimeMillis()
            dataBean = TaskWithStep(task, mutableListOf())
            Log.d(TAG, "initData: add new task")
            updateView()
        } else {
            Log.d(TAG, "initData: edit old task $taskId")
            viewModel.loadTaskWithSteps(taskId)
                .flowOn(Dispatchers.IO)
                .collect { data ->
                    dataBean = data
                    updateView()
                }
        }
    }

    private fun updateView() {
        with(viewBinding) {
            dataBean?.let {
                val task = it.task
                edtTitle.setText(task.title)
//                editTaskEdtRemark.setText(task.content)
                adapter?.submitList(it.steps)
            }
        }
    }

    private fun saveTask() = lifecycleScope.launch {
        val taskName = viewBinding.edtTitle.text.toString()
        if (taskName.isEmpty()) {
            return@launch
        }
        Log.d(TAG, "saveTask: $taskName")
        dataBean?.let {
            with(it.task) {
                checklistId = this@EditTaskFragment.checklistId
                priority = getPriority()
                title = taskName
            }
            viewModel.addTask(it)
        }
        findNavController().navigateUp()
    }

    private fun getPriority(): Int {
        if (viewBinding.spPriority.selectedItemPosition == 0) {
            return 2
        }
        return 1
    }
}
