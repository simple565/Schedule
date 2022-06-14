package com.maureen.schedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maureen.schedule.database.Step
import com.maureen.schedule.databinding.ItemStepBinding
import com.maureen.schedule.entity.Status

/**
 * Function:
 *
 * @author lianml
 * Create 2021-10-17
 */
class StepAdapter : ListAdapter<Step, StepAdapter.ViewHolder>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Step>() {
            override fun areItemsTheSame(oldItem: Step, newItem: Step): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Step, newItem: Step): Boolean {
                return oldItem.status == newItem.status && oldItem.name == newItem.name
            }
        }
    }

    private var delAction: ((Step) -> Unit)? = null

    fun setStepDeleteListener(delAction: (Step) -> Unit){
        this.delAction = delAction
    }

    class ViewHolder(private val viewBinding: ItemStepBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        val statusCb = viewBinding.itemCbStepStatus
        val clearIv = viewBinding.itemIvClearStep
        fun bind(data: Step) {
            with(viewBinding) {
                itemTvStepTitle.text = data.name
                itemCbStepStatus.isChecked = data.status == Status.DONE.ordinal
                itemCbStepStatus.setOnClickListener {
                    data.status = if((it as CheckBox).isChecked) Status.DONE.ordinal else Status.DOING.ordinal
                    data.finishTime = System.currentTimeMillis()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(ItemStepBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        viewHolder.statusCb.setOnClickListener {
            val isChecked = (it as CheckBox).isChecked
            val step = getItem(viewHolder.adapterPosition)
            step.status = if(isChecked) Status.DONE.ordinal else Status.DONE.ordinal
            if (isChecked) {
                step.finishTime = System.currentTimeMillis()
            } else {
                step.finishTime = 0
            }
            notifyItemChanged(viewHolder.adapterPosition)
        }
        viewHolder.clearIv.setOnClickListener {
            delAction?.invoke(getItem(viewHolder.adapterPosition))
            notifyItemRemoved(viewHolder.adapterPosition)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}