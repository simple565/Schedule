package com.maureen.schedule.database

import androidx.room.*

/**
 * Function:
 * @author lianml
 * Create 2021-10-16
 */
@Dao
interface StepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStep(step: Step): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSteps(steps: List<Step>): List<Long>

    @Delete
    suspend fun deleteStep(step: Step): Int

    @Delete
    suspend fun deleteSteps(steps: List<Step>)

    @Update
    suspend fun updateStep(step: Step): Int
}