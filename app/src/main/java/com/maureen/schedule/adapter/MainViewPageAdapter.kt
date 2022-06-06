package com.maureen.schedule.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.maureen.schedule.view.task.EditChecklistFragment
import com.maureen.schedule.view.task.TasksFragment

/**
 * Function:
 * @author lianml
 * Create 2021-12-22
 */
const val INDEX_TASKS = 0
const val INDEX_CALENDAR = 1
const val INDEX_SETTINGS = 2

class MainViewPageAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private val fragmentMap = mapOf(
        INDEX_TASKS to { TasksFragment() },
        INDEX_CALENDAR to { EditChecklistFragment() },
        INDEX_SETTINGS to { EditChecklistFragment() }
    )

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentMap[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

}