package com.maureen.schedule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maureen.schedule.R
import com.maureen.schedule.databinding.ItemChecklistBinding
import com.maureen.schedule.entity.ChecklistWithTask
import com.maureen.schedule.utils.KEY_CHECKLIST_ID

/**
 * Function:清单列表适配器
 * @author lianml
 * Create 2022-05-19
 */
class ChecklistAdapter : ListAdapter<ChecklistWithTask, ChecklistAdapter.ViewHolder>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<ChecklistWithTask>() {
            override fun areItemsTheSame(oldItem: ChecklistWithTask, newItem: ChecklistWithTask): Boolean {
                return oldItem.checklist.id == newItem.checklist.id
            }

            override fun areContentsTheSame(oldItem: ChecklistWithTask, newItem: ChecklistWithTask): Boolean {
                return oldItem.toString() == newItem.toString()
            }
        }
    }

    private val itemClickAction: (View, ChecklistWithTask) -> Unit =  { view, checklistWithTask ->
        view.findNavController().navigate(R.id.taskListScreen, bundleOf(KEY_CHECKLIST_ID to checklistWithTask.checklist.id))
    }

    class ViewHolder(private val viewBinding: ItemChecklistBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(data: ChecklistWithTask, itemClickAction: (View, ChecklistWithTask) -> Unit) {
            viewBinding.tvChecklistName.text = data.checklist.name
            viewBinding.tvTaskCount.text = data.tasks.size.toString()
            viewBinding.root.setOnClickListener { itemClickAction.invoke(it, data) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(ItemChecklistBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), itemClickAction)
    }
}