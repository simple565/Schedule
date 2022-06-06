package com.maureen.schedule.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

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
    fun getAllCourses(): Flow<List<CourseInfo>>

    /**
     * 查找指定id的课程信息
     *
     * @param id 课程id
     * @return 课程信息
     */
    @Query("select * from course where id = :id")
    fun getCourseInfoById(id: Int): CourseInfo

    /**
     * 插入单条课程数据
     *
     * @param course 课程信息
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCourseInfo(course: CourseInfo)

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
     * @param courseInfo
     */
    @Delete
    fun delCourse(courseInfo: CourseInfo)

    /**
     * 批量更新课程信息
     *
     * @param courseInfo 课程信息列表
     * @return
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCourse(courseInfo: CourseInfo): Int
}