package com.maureen.schedule.view.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.maureen.schedule.adapter.ChecklistAdapter
import com.maureen.schedule.database.AppDatabase
import com.maureen.schedule.databinding.FragmentChecklistBinding
import com.maureen.schedule.entity.ChecklistWithTask
import com.maureen.schedule.view.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class EditChecklistFragment : BaseFragment() {
    private lateinit var viewBinding: FragmentChecklistBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewBinding = FragmentChecklistBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       /* val adapter = ChecklistAdapter()
        viewBinding.rvChecklists.adapter = adapter
        lifecycleScope.launch {
            val list = mutableListOf<ChecklistWithTask>()
            AppDatabase.getInstance().checklistDao().getAllChecklistWithTask()
                .flowOn(Dispatchers.IO)
                .onEach {
                    list.addAll(it)
                }
        }*/
    }
}