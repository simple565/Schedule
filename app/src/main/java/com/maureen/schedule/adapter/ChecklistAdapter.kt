package com.maureen.schedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maureen.schedule.database.Checklist
import com.maureen.schedule.databinding.ItemChecklistBinding
import com.maureen.schedule.entity.ChecklistWithTask

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
                return oldItem == newItem
            }
        }
    }

    var itemClickAction: ((Long) -> Unit)? = null

    class ViewHolder(private val viewBinding: ItemChecklistBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(data: ChecklistWithTask, clickAction: ((Long) -> Unit)?) {
            viewBinding.root.setOnClickListener {
                clickAction?.invoke(data.checklist.id)
            }
            viewBinding.tvChecklistName.text = String.format("%s(%d)",data.checklist.name, data.tasks.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(
            ItemChecklistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), itemClickAction)
    }
}