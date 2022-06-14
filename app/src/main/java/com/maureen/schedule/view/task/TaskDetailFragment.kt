package com.maureen.schedule.view.task

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.switchmaterial.SwitchMaterial
import com.maureen.schedule.R
import com.maureen.schedule.adapter.StepAdapter
import com.maureen.schedule.database.Task
import com.maureen.schedule.databinding.FragmentEditTaskBinding
import com.maureen.schedule.entity.TaskPriority
import com.maureen.schedule.entity.TaskWithStep
import com.maureen.schedule.utils.KEY_CHECKLIST_ID
import com.maureen.schedule.utils.KEY_TASK_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


class TaskDetailFragment : Fragment() {
    companion object {
        private const val TAG = "TaskDetailFragment"
    }

    private lateinit var viewBinding: FragmentEditTaskBinding
    private val viewModel: TaskViewModel by viewModels()
    private var taskWithStep: TaskWithStep? = null
    private var stepAdapter: StepAdapter = StepAdapter()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
        viewBinding.btnImportant.setOnClickListener {
            taskWithStep?.let { item ->
                item.task.priority = if ((it as SwitchMaterial).isChecked) TaskPriority.HIGH.value else TaskPriority.NORMAL.value
            }
        }
        /*viewBinding.rvStep.adapter = stepAdapter
        viewBinding.edtStep.setOnEditorActionListener { v, actionId, _ ->
            Log.d(TAG, "initView: $actionId")
            if (v.text.isNotEmpty() && actionId == EditorInfo.IME_ACTION_NEXT) {
                val step = Step()
                step.name = v.text.toString()
                step.createTime = System.currentTimeMillis()
                taskItem?.let {
                    it.steps.add(step)
                    stepAdapter.notifyItemInserted(it.steps.size)
                }
            }
            v.text = ""
            v.clearFocus()
            false
        }*/
        viewBinding.btnSave.setOnClickListener { saveTask() }
    }

    private fun initData() = lifecycleScope.launch {
        arguments?.let {
            val checklistId = it.getLong(KEY_CHECKLIST_ID, 1)
            val taskId = it.getLong(KEY_TASK_ID, -1)
            if (-1L == taskId) {
                Log.d(TAG, "initData: add new task")
                viewBinding.toolBar.title = getString(R.string.add_task)
                val task = Task().apply {
                    this.checklistId = checklistId
                    createTime = System.currentTimeMillis()
                }
                taskWithStep = TaskWithStep(task, mutableListOf())
                updateView(taskWithStep!!)
            } else {
                Log.d(TAG, "initData: edit old task $taskId")
                viewBinding.toolBar.title = getString(R.string.edit_task)
                viewModel.loadTaskWithSteps(taskId)
                    .flowOn(Dispatchers.IO)
                    .collect { data ->
                        taskWithStep = data
                        updateView(data)
                    }
            }
        }
    }

    private fun updateView(data: TaskWithStep) {
        data.let {
            viewBinding.edtTitle.setText(it.task.title)
            viewBinding.btnImportant.isChecked = it.task.priority == TaskPriority.HIGH.value
            stepAdapter.submitList(it.steps)
        }
    }

    private fun saveTask() = lifecycleScope.launch {
        val taskName = viewBinding.edtTitle.text.toString()
        if (taskName.isEmpty()) {
            return@launch
        }
        Log.d(TAG, "saveTask: $taskName")
        taskWithStep?.let {
            it.task.apply {
                title = taskName
                /*priority = if (viewBinding.chipPriorityHigh.isChecked) {
                    TaskPriority.HIGH.value
                } else {
                    TaskPriority.NORMAL.value
                }*/

            }
            viewModel.addTask(it)
        }
        findNavController().navigateUp()
    }
}
