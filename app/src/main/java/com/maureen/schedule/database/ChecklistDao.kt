package com.maureen.schedule.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.maureen.schedule.entity.ChecklistWithTask
import kotlinx.coroutines.flow.Flow

/**
 * Function:
 * @author lianml
 * Create 2021-10-16
 */
@Dao
interface ChecklistDao {

    @Query("select exists(select * from checklist where id = :id limit 1)")
    suspend fun existChecklist(id: Long):Boolean

    @Query("select exists(select * from checklist limit 1)")
    suspend fun hasChecklist():Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addChecklist(checklist: Checklist): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addChecklists(checklists: List<Checklist>): List<Long>

    @Delete
    suspend fun deleteChecklist(checklist: Checklist): Int

    @Update
    suspend fun updateChecklist(checklist: Checklist): Int

    @Query("select * from checklist where id = :id")
    fun getChecklist(id: Long):Checklist

    @Query("select * from checklist")
    fun getChecklists(): LiveData<List<Checklist>>

    @Transaction
    @Query("select * from checklist where id = :id")
    fun getChecklistWithTask(id: Long): LiveData<ChecklistWithTask>

    @Transaction
    @Query("select * from checklist")
    fun getAllChecklistWithTask(): LiveData<List<ChecklistWithTask>>
}