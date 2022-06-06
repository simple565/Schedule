package com.maureen.schedule.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Function: 清单实体类
 * @author lianml
 * Create 2021-10-10
 */
@Entity(tableName = "checklist")
data class Checklist(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String = "",
    @ColumnInfo(name = "create_time")
    var createTime: Long = -1
)
