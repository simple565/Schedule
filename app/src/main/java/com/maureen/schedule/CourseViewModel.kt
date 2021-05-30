package com.maureen.schedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.maureen.schedule.data.AppDatabase
import com.maureen.schedule.data.CourseInfoBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Function:
 * Create:   2020/11/26
 *
 * @author lianml
 */
class CourseViewModel : ViewModel() {
    val courseLiveData = MutableLiveData<CourseInfoBean>()

    val courseListLiveData = liveData(Dispatchers.IO) {
        val data = AppDatabase.getInstance().courseInfoDao().getAllCourses()
        emitSource(data)
    }

    fun findCourseById(courseId: Int) = viewModelScope.launch(Dispatchers.IO) {
        val data = AppDatabase.getInstance().courseInfoDao().getCourseInfoById(courseId)
        data?.let {
            courseLiveData.postValue(data!!)
        } ?: run {
            courseLiveData.postValue(CourseInfoBean())
        }

    }

    fun saveCourseInfo(courseInfoBean: CourseInfoBean) = viewModelScope.launch(Dispatchers.IO) {
        AppDatabase.getInstance().courseInfoDao().saveCourseInfo(courseInfoBean)
    }

    fun updateCourseInfo(courseInfoBean: CourseInfoBean) = viewModelScope.launch(Dispatchers.IO) {
        AppDatabase.getInstance().courseInfoDao().updateCourse(courseInfoBean)
    }

    fun deleteCourseInfo(courseInfoBean: CourseInfoBean) = viewModelScope.launch(Dispatchers.IO) {
        AppDatabase.getInstance().courseInfoDao().delCourse(courseInfoBean)
    }
}