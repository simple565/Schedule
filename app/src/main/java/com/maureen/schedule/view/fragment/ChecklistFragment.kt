package com.maureen.schedule.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.maureen.schedule.ChecklistViewModel
import com.maureen.schedule.R
import com.maureen.schedule.adapter.ChecklistAdapter
import com.maureen.schedule.databinding.FragmentChecklistBinding
import com.maureen.schedule.entity.Status
import com.maureen.schedule.utils.ItemSwipeHelper
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ChecklistFragment : Fragment() {
    companion object{
        private const val TAG = "ChecklistFragment"
    }

    private val viewModel by viewModels<ChecklistViewModel>()
    private lateinit var viewBinding: FragmentChecklistBinding
    private val adapter = ChecklistAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewBinding = FragmentChecklistBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initDate()
        viewBinding.tvAddChecklist.setOnClickListener {
            findNavController().navigate(R.id.editChecklistDialog)
        }
        viewBinding.rvChecklists.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(ItemSwipeHelper())
        itemTouchHelper.attachToRecyclerView(viewBinding.rvChecklists)

        observeData()
    }

    /**
     * 初始化日期
     */
    private fun initDate() {
        val time = System.currentTimeMillis()
        viewBinding.toolBar.title = SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA).format(time)
        viewBinding.toolBar.subtitle = SimpleDateFormat("EE", Locale.CHINA).format(time)
    }

    private fun observeData() {
        viewModel.loadChecklists().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            Log.d(TAG, "observeData: ${it.size}")
            val allTask = it.flatMap { checklist -> checklist.tasks }
            viewBinding.tvAllCount.text = "${ allTask.size }"
            viewBinding.tvCompleteCount.text = "${allTask.filter { task -> task.status == Status.DONE.value}.size}"
            viewBinding.tvDoingCount.text = "${allTask.filter { task -> task.status == Status.DOING.value}.size}"
        }
    }
}