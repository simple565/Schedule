package com.maureen.schedule.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.maureen.schedule.TaskViewModel
import com.maureen.schedule.adapter.TaskAdapter
import com.maureen.schedule.databinding.FragmentTasksBinding

class TasksFragment : Fragment() {
    companion object {
        private const val TAG = "TasksFragment"
    }

    private lateinit var viewBinding: FragmentTasksBinding
    private val viewModel by activityViewModels<TaskViewModel>()
    private var curChecklistId = -1L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewBinding = FragmentTasksBinding.inflate(inflater, container, false)
        return viewBinding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = TaskAdapter(finishAction = { viewModel.updateTask(it) })
            .apply { onItemLongClickListener ={ } }
        viewBinding.rvTask.adapter = adapter
        viewModel.checklistWithTaskLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "initData: ${it.checklist.name}")
            curChecklistId = it.checklist.id
            viewBinding.toolbar.title = it.checklist.name
            adapter.submitList(it.tasks)
        }
        viewModel.loadChecklistWithTasks(1)
    }
}