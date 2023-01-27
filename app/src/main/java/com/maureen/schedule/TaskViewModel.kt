package com.maureen.schedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maureen.schedule.database.AppDatabase
import com.maureen.schedule.database.Task
import com.maureen.schedule.entity.Status
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

    fun loadChecklistWithTasks(id: Long) = AppDatabase.getInstance().checklistDao().getChecklistWithTask(id)

    fun loadTaskWithSteps(id: Long) = AppDatabase.getInstance().taskDao().getTaskWithStep(id)

    suspend fun saveTask(taskWithStep: TaskWithStep) = withContext(Dispatchers.IO) {
        AppDatabase.getInstance().taskDao().addTask(taskWithStep.task)
        AppDatabase.getInstance().stepDao().addSteps(taskWithStep.steps)
    }

    suspend fun deleteTask(taskWithStep: TaskWithStep) = withContext(Dispatchers.IO) {
        val taskId = AppDatabase.getInstance().taskDao().deleteTask(taskWithStep.task)
        Log.d(TAG, "deleteTask: delete task id $taskId")
        AppDatabase.getInstance().stepDao().deleteSteps(taskWithStep.steps)
    }

    fun updateTaskStatus(task: Task, isFinish: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        task.let {
            it.status = if (isFinish) Status.DONE.value else Status.DOING.value
            it.finishTime = if (isFinish) System.currentTimeMillis() else 0
            AppDatabase.getInstance().taskDao().updateTask(it)
        }
    }
}