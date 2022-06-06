package com.maureen.schedule.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maureen.schedule.entity.Status

/**
 * Function: 步骤
 * @author lianml
 * Create 2021-10-10
 */
@Entity(tableName = "step")
data class Step(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "task_id")
    var taskId: Long = -1,
    var name: String = "",
    var status: Int = Status.DOING.ordinal,
    @ColumnInfo(name = "create_time")
    var createTime: Long = -1,
    @ColumnInfo(name = "finish_time")
    var finishTime: Long = -1
)
