package com.maureen.schedule.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.maureen.schedule.ChecklistViewModel
import com.maureen.schedule.R
import com.maureen.schedule.TaskViewModel
import com.maureen.schedule.adapter.ChecklistAdapter
import com.maureen.schedule.databinding.DialogEditChecklistBinding
import com.maureen.schedule.utils.KEY_CHECKLIST_ID
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Function: 新增/编辑清单弹窗
 * @author lianml
 * Create 2022-06-03
 */
class EditChecklistDialog : DialogFragment() {
    companion object {
        private const val TAG = "EditChecklistDialog"
    }

    private lateinit var viewBinding: DialogEditChecklistBinding
    private val viewModel by viewModels<ChecklistViewModel>()

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_white_corner_10)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewBinding = DialogEditChecklistBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        val checklistId = arguments?.getLong(KEY_CHECKLIST_ID, -1) ?: -1
        Log.d(TAG, "onViewCreated: $checklistId")
        viewModel.loadChecklist(checklistId)
        viewModel.checklistLiveData.observe(viewLifecycleOwner) {
            viewBinding.edtName.setText(it.name)
        }
    }

    private fun initView() {
        viewBinding.btnCancel.setOnClickListener { dismiss() }
        viewBinding.btnSave.setOnClickListener {
            viewModel.checklistLiveData.value?.let {
                it.name = viewBinding.edtName.text.toString()
                viewModel.saveChecklist(it)
            }
            dismiss()
        }
    }

}