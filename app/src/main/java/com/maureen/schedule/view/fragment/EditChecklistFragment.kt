package com.maureen.schedule.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.maureen.schedule.databinding.FragmentChecklistBinding

class EditChecklistFragment : Fragment() {
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