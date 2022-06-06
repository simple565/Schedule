package com.maureen.schedule.database

import androidx.room.*
import com.maureen.schedule.entity.TaskWithStep
import kotlinx.coroutines.flow.Flow

/**
 * Function:
 * Date:   2021/6/25
 * @author lianml
 */
@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: Task): Long

    @Delete
    suspend fun deleteTask(task: Task): Int

    @Update
    suspend fun updateTask(task: Task): Int

    @Query("select * from task")
    fun getTask(): Flow<List<Task>>

    @Transaction
    @Query("select * from task where id = :id")
    fun getTaskWithStep(id: Long): Flow<TaskWithStep>
}