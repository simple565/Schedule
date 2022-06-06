package com.maureen.schedule.view.task

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maureen.schedule.adapter.ChecklistAdapter
import com.maureen.schedule.databinding.DialogBottomSheetMoreActionBinding
import com.maureen.schedule.databinding.DialogBottomSheetSelectChecklistBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Function: 更多操作底部弹窗
 * @author lianml
 * Create 2022-06-03
 */
class MoreActionBottomSheetDialog : BottomSheetDialogFragment() {
    companion object {
        private const val TAG = "MoreActionBottomSheetDialog"
    }

    private lateinit var viewBinding: DialogBottomSheetMoreActionBinding
    private val viewModel by activityViewModels<TaskViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = DialogBottomSheetMoreActionBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")

    }


}