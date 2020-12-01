package com.maureen.schedule;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.maureen.schedule.data.AppDatabase;
import com.maureen.schedule.data.CourseInfoBean;

import java.util.List;

/**
 * Function:
 * Create:   2020/11/26
 *
 * @author lianml
 */
public class CourseViewModel extends ViewModel {

    private LiveData<List<CourseInfoBean>> mCourseInfoLiveData;

    public LiveData<List<CourseInfoBean>> getCourseInfoLiveData() {
        if (null == mCourseInfoLiveData) {
            mCourseInfoLiveData = AppDatabase.getInstance().getCourseInfoDao().getAllCourses();
        }
        return mCourseInfoLiveData;
    }

    public void saveCourseInfo(CourseInfoBean courseInfoBean) {

        AppDatabase.getInstance().getCourseInfoDao().saveCourseInfo(courseInfoBean);
    }

    public void deleteCourseInfo(CourseInfoBean courseInfoBean) {
        AppDatabase.getInstance().getCourseInfoDao().delCourse(courseInfoBean);
    }
}
