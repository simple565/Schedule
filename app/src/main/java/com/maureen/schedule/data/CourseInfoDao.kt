package com.maureen.schedule.data

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Function:
 * Create:   2020/11/24
 *
 * @author lianml
 */
@Dao
interface CourseInfoDao {
    /**
     * 查询所有课程
     *
     * @return 所有课程列表
     */
    @Query("select * from course")
     fun getAllCourses(): LiveData<List<CourseInfoBean>>

    /**
     * 查找指定id的课程信息
     *
     * @param id 课程id
     * @return 课程信息
     */
    @Query("select * from course where id = :id")
    fun getCourseInfoById(id: Int): CourseInfoBean

    /**
     * 插入单条课程数据
     *
     * @param course 课程信息
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCourseInfo(course: CourseInfoBean)

    /**
     * 根据名称删除指定课程
     *
     * @param id 课程id
     */
    @Query("delete from course where name = :id")
     fun delCourseById(id: Int)

    /**
     * 删除指定课程
     *
     * @param courseInfoBean
     */
    @Delete
     fun delCourse(courseInfoBean: CourseInfoBean)

    /**
     * 批量更新课程信息
     *
     * @param courseInfoBean 课程信息列表
     * @return
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
     fun updateCourse(courseInfoBean: CourseInfoBean): Int
}