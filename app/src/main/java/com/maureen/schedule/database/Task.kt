package com.maureen.schedule.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maureen.schedule.entity.TaskPriority
import com.maureen.schedule.entity.Status
import com.maureen.schedule.entity.TaskType

/**
 * Function:
 * Date:   2021/6/22
 * @author lianml
 */
@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "checklist_id")
    var checklistId: Long = -1L,
    @ColumnInfo(name = "name")
    var title: String = "",
    @ColumnInfo(name = "remark")
    var content: String = "",
    @ColumnInfo(name = "create_time")
    var createTime: Long = -1L,
    @ColumnInfo(name = "finish_time")
    var finishTime: Long = -1L,
    /**
     * 截止日期
     */
    @ColumnInfo(name = "deadline")
    var date: String = "",

    /**
     * 类别
     * 工作1；生活2；娱乐3；
     */
    var type: Int = TaskType.LIFE.value,

    /**
     * 优先级
     * 重要（1）；一般（2）等
     */
    var priority: Int = TaskPriority.NORMAL.value,
    /**
     * 状态
     * 0 未完成；1 完成；2 计划中
     */
    var status: Int = Status.PLANING.value
)
