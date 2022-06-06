package com.maureen.schedule.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.maureen.schedule.database.Checklist
import com.maureen.schedule.database.Task

/**
 * Function:
 * @author lianml
 * Create 2021-10-16
 */
data class ChecklistWithTask(
    @Embedded
    val checklist: Checklist,
    @Relation(parentColumn = "id", entityColumn = "checklist_id")
    val tasks: MutableList<Task>
)
