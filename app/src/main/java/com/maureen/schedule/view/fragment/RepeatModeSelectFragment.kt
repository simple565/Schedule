package com.maureen.schedule.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maureen.schedule.databinding.FragmentRepeatModeSelectListDialogBinding

const val ARG_ITEM_COUNT = "item_count"

/**
 * Function:重复模式选择底部对话框
 * @author lianml
 * Create 2022-06-18
 */
class RepeatModeSelectFragment : BottomSheetDialogFragment() {
    companion object {

        private const val TAG = "RepeatModeSelectFragment"

        fun newInstance(itemCount: Int): RepeatModeSelectFragment =
            RepeatModeSelectFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ITEM_COUNT, itemCount)
                }
            }

    }
    private var _binding: FragmentRepeatModeSelectListDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRepeatModeSelectListDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}