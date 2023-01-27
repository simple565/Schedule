package com.maureen.schedule.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.switchmaterial.SwitchMaterial
import com.maureen.schedule.R
import com.maureen.schedule.TaskViewModel
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
    private var taskWithStep = TaskWithStep(Task(createTime = System.currentTimeMillis()), mutableListOf())
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
        viewBinding.btnSave.setOnClickListener { saveTask() }
    }

    private fun initData() {
        val checklistId = arguments?.getLong(KEY_CHECKLIST_ID, -1) ?: -1
        val taskId = arguments?.getLong(KEY_TASK_ID, -1) ?: -1
        if (-1L == taskId) {
            Log.d(TAG, "initData: add new task")
            viewBinding.toolBar.title = getString(R.string.add_task)
            taskWithStep.task.checklistId = checklistId
            updateView()
        } else {
            Log.d(TAG, "initData: edit old task $taskId")
            viewBinding.toolBar.title = getString(R.string.edit_task)
            viewModel.loadTaskWithSteps(taskId)
                .observe(viewLifecycleOwner) {
                    taskWithStep = it
                    updateView()
                }
        }
    }

    private fun updateView() {
        viewBinding.edtTitle.setText(taskWithStep.task.title)
        viewBinding.swtImportant.isChecked = taskWithStep.task.priority == TaskPriority.HIGH.value
        stepAdapter.submitList(taskWithStep.steps)
    }

    private fun saveTask() = lifecycleScope.launch {
        val taskName = viewBinding.edtTitle.text.toString()
        if (taskName.isEmpty()) {
            return@launch
        }
        taskWithStep.task.title = taskName
        taskWithStep.task.priority = if (viewBinding.swtImportant.isChecked) TaskPriority.HIGH.value else TaskPriority.NORMAL.value
        Log.d(TAG, "saveTask: $taskWithStep")
        viewModel.saveTask(taskWithStep)
        findNavController().navigateUp()
    }
}
