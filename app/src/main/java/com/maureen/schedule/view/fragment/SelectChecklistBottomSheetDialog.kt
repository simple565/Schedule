package com.maureen.schedule.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maureen.schedule.R
import com.maureen.schedule.TaskViewModel
import com.maureen.schedule.adapter.ChecklistAdapter
import com.maureen.schedule.databinding.DialogBottomSheetSelectChecklistBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Function: 选择清单底部弹窗
 * @author lianml
 * Create 2022-06-03
 */
class SelectChecklistBottomSheetDialog : BottomSheetDialogFragment() {
    companion object {
        private const val TAG = "SelectChecklistBottomSheetDialog"
    }

    private lateinit var viewBinding: DialogBottomSheetSelectChecklistBinding
    private val viewModel by activityViewModels<TaskViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = DialogBottomSheetSelectChecklistBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        val adapter = ChecklistAdapter().apply {
            itemClickAction = { checklistId ->
                viewModel.loadChecklistWithTasks(checklistId)
                dismiss()
            }
        }
        viewBinding.rvChecklists.adapter = adapter
        viewModel.loadAllChecklistWithTask().onEach {
            adapter.submitList(it)
        }.launchIn(lifecycleScope)
        viewBinding.btnAddChecklist.setOnClickListener {
            dismiss()
        }
    }


}