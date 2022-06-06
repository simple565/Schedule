package com.maureen.schedule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maureen.schedule.R
import com.maureen.schedule.database.Task
import com.maureen.schedule.databinding.ItemTaskBinding
import com.maureen.schedule.utils.KEY_TASK_ID
import com.maureen.schedule.utils.convertToDate

/**
 * Function: 任务列表适配器
 * @author lianml
 * Create 2021-10-17
 */
class TaskAdapter(private val finishAction: (Task) -> Unit) :
    ListAdapter<Task, TaskAdapter.ViewHolder>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(private val viewBinding: ItemTaskBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        val cbFinishStatus = viewBinding.cbTaskStatus
        val rootView = viewBinding.root
        fun bind(data: Task) {
            with(viewBinding) {
                cbTaskStatus.isChecked = data.status == 1
                if (-1L == data.finishTime) {
                    tvTaskDeadline.visibility = View.GONE
                } else {
                    tvTaskDeadline.visibility = View.VISIBLE
                }
                tvTaskDeadline.text = data.finishTime.convertToDate("yyyy年MM月dd日")
                tvTaskTitle.text = data.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            cbFinishStatus.setOnClickListener {
                val data = getItem(adapterPosition)
                it as CheckBox
                data.status = if (it.isChecked) 1 else 0
                data.finishTime = System.currentTimeMillis()
                finishAction.invoke(data)
            }
            rootView.setOnClickListener {
                val data = getItem(adapterPosition)
                val bundle = bundleOf(KEY_TASK_ID to data.id)
                it.findNavController().navigate(R.id.action_to_edit_task, bundle)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}