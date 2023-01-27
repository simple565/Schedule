package com.maureen.schedule

import androidx.lifecycle.*
import com.maureen.schedule.database.AppDatabase
import com.maureen.schedule.database.Checklist
import com.maureen.schedule.database.ChecklistDao
import com.maureen.schedule.entity.ChecklistWithTask
import com.maureen.schedule.utils.KEY_CHECKLIST_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Description: 清单相关页面ViewModel
 * @author Maureen
 * Create 2023-01-23 11:09
 */
class ChecklistViewModel : ViewModel() {

    val checklistLiveData = MutableLiveData<Checklist>()

    fun loadChecklist(id: Long) = viewModelScope.launch(Dispatchers.IO){
        if (id == -1L ){
            checklistLiveData.postValue(Checklist(createTime = System.currentTimeMillis()))
        } else {
            checklistLiveData.postValue(AppDatabase.getInstance().checklistDao().getChecklist(id))
        }
    }

    fun loadChecklists() : LiveData<List<ChecklistWithTask>> {
        return AppDatabase.getInstance().checklistDao().getAllChecklistWithTask()
    }

    fun saveChecklist(checklist: Checklist) = viewModelScope.launch(Dispatchers.IO) {
        AppDatabase.getInstance().checklistDao().run {
            if (existChecklist(checklist.id)) {
                updateChecklist(checklist)
            } else {
                addChecklist(checklist)
            }
        }
    }
}