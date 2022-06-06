package com.maureen.schedule.view.task

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maureen.schedule.database.AppDatabase
import com.maureen.schedule.database.Checklist
import com.maureen.schedule.database.Task
import com.maureen.schedule.entity.ChecklistWithTask
import com.maureen.schedule.entity.TaskWithStep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Function:
 * @author lianml
 * Create 2021-10-17
 */
class TaskViewModel : ViewModel() {
    companion object {
        private const val TAG = "TaskViewModel"
    }

    val checklistWithTaskLiveData = MutableLiveData<ChecklistWithTask>()

    fun loadTaskWithSteps(id: Long) = AppDatabase.getInstance().taskDao().getTaskWithStep(id)

    suspend fun addTask(data: TaskWithStep) = withContext(Dispatchers.IO) {
        var taskId = data.task.id
        if (taskId == 0L) {
            taskId = AppDatabase.getInstance().taskDao().addTask(data.task)
        } else {
            AppDatabase.getInstance().taskDao().updateTask(data.task)
        }
        Log.d(TAG, "addTask: task id $taskId")
        data.steps.forEach { step -> step.taskId = taskId }
        val count = AppDatabase.getInstance().stepDao().addSteps(data.steps)
        Log.d(TAG, "addTask: step count $count")
    }

    suspend fun deleteTask(taskWithStep: TaskWithStep?) = withContext(Dispatchers.IO) {
        taskWithStep?.let {
            val taskId = AppDatabase.getInstance().taskDao().deleteTask(it.task)
            Log.d(TAG, "deleteTask: delete task id $taskId")
            AppDatabase.getInstance().stepDao().deleteSteps(it.steps)
        }
    }

    fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        AppDatabase.getInstance().taskDao().updateTask(task)
    }

    fun loadAllChecklistWithTask() = AppDatabase.getInstance().checklistDao().getAllChecklistWithTask()

    fun loadChecklistWithTasks(checklistId: Long) = viewModelScope.launch(Dispatchers.IO) {
        checklistWithTaskLiveData.postValue(
            AppDatabase.getInstance().checklistDao().getChecklistWithTask(checklistId)
        )
    }

    suspend fun addChecklist(checklist: Checklist) = withContext(Dispatchers.IO) {
        AppDatabase.getInstance().checklistDao().addChecklist(checklist)
    }

    /**
     * 初始化三个基础清单 工作、生活、娱乐
     */
    fun initChecklist() = viewModelScope.launch(Dispatchers.IO) {
        AppDatabase.getInstance().checklistDao().run {
            if (!hasChecklist()) {
                val createTime = System.currentTimeMillis()
                val checklists = listOf(
                    Checklist(name = "工作", createTime = createTime),
                    Checklist(name = "生活", createTime = createTime),
                    Checklist(name = "娱乐", createTime = createTime)
                )
                AppDatabase.getInstance().checklistDao().addChecklists(checklists)
                Log.d(TAG, "initChecklist: ")
            }
        }
    }
}