package com.maureen.schedule.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.maureen.schedule.database.Step
import com.maureen.schedule.database.Task

/**
 * Function:
 * @author lianml
 * Create 2021-10-16
 */
data class TaskWithStep(
    @Embedded
    val task: Task,
    @Relation(parentColumn = "id", entityColumn = "task_id")
    val steps: MutableList<Step>
)
