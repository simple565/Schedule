package com.maureen.schedule.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.maureen.schedule.R
import com.maureen.schedule.TaskViewModel
import com.maureen.schedule.adapter.TaskAdapter
import com.maureen.schedule.databinding.FragmentTasksBinding
import com.maureen.schedule.entity.Status
import com.maureen.schedule.utils.KEY_CHECKLIST_ID
import java.text.NumberFormat

class TasksFragment : Fragment() {
    companion object {
        private const val TAG = "TasksFragment"
    }

    private lateinit var viewBinding: FragmentTasksBinding
    private val viewModel by viewModels<TaskViewModel>()
    private val taskAdapter = TaskAdapter()
    private var curChecklistId = -1L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewBinding = FragmentTasksBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.rvTask.adapter = taskAdapter
        curChecklistId = arguments?.getLong(KEY_CHECKLIST_ID, -1L) ?: -1L
        viewBinding.fabAddTask.setOnClickListener {
            findNavController().navigate(R.id.editTaskScreen, bundleOf(KEY_CHECKLIST_ID to curChecklistId))
        }
        Log.d(TAG, "onViewCreated: checklist id $curChecklistId")
        viewModel.loadChecklistWithTasks(curChecklistId).observe(viewLifecycleOwner){
            Log.d(TAG, "onViewCreated: task count ${it.tasks.size}")
            viewBinding.toolbar.title = it.checklist.name
            taskAdapter.submitList(it.tasks)
            val completedCount = it.tasks.filter { task -> task.status == Status.DONE.value }.size
            viewBinding.tvPercent.text = NumberFormat.getPercentInstance().format(completedCount / it.tasks.size)
        }
    }
}