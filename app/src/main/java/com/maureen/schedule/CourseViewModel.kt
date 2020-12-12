package com.maureen.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.maureen.schedule.data.AppDatabase
import com.maureen.schedule.data.CourseInfoBean

/**
 * Function:
 * Create:   2020/11/26
 *
 * @author lianml
 */
class CourseViewModel : ViewModel() {

    fun getAllCourseInfo(): LiveData<List<CourseInfoBean>> {
        return AppDatabase.getInstance().courseInfoDao().getAllCourses()
    }

    fun findCourseById(courseId: Int): CourseInfoBean? {
        return AppDatabase.getInstance().courseInfoDao().getCourseInfoById(courseId)
    }

    fun saveCourseInfo(courseInfoBean: CourseInfoBean) {
        AppDatabase.getInstance().courseInfoDao().saveCourseInfo(courseInfoBean)
    }

    fun updateCourseInfo(courseInfoBean: CourseInfoBean) {
        AppDatabase.getInstance().courseInfoDao().updateCourse(courseInfoBean)
    }

    fun deleteCourseInfo(courseInfoBean: CourseInfoBean) {
        AppDatabase.getInstance().courseInfoDao().delCourse(courseInfoBean)
    }
}