package com.maureen.schedule.utils

/**
 * Function:
 * Create:   2020/12/8
 * @author lianml
 */

const val DATABASE_NAME = "Schedule.db"

const val KEY_TASK_ID = "TaskId"
const val KEY_CHECKLIST_ID = "ChecklistId"

const val WEEK_LENGTH = 7

const val TOTAL_COURSE_COUNT = 10

val TASK_PRIORITY_MAP = mapOf(1 to "重要", 2 to "一般")
val TASK_TYPE_MAP = mapOf(1 to "工作", 2 to "生活", 3 to "娱乐")