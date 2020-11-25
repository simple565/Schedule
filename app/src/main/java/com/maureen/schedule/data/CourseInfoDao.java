package com.maureen.schedule.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Function:
 * Create:   2020/11/24
 *
 * @author lianml
 */
@Dao
public interface CourseInfoDao {

    /**
     * 查询所有课程
     *
     * @return 所有课程列表
     */
    @Query("select * from course")
    List<CourseInfoBean> getAllCourses();


    /**
     * 插入单条课程数据
     *
     * @param course 课程信息
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveCourseInfo(CourseInfoBean course);

    /**
     * 批量插入课程数据
     *
     * @param courseInfoBeans 课程信息列表
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveCourseInfoList(List<CourseInfoBean> courseInfoBeans);

    /**
     * 根据名称删除指定课程
     *
     * @param name 课程名称
     */
    @Query("delete from course where name = :name")
    void delCourseByName(String name);

    /**
     * 删除指定课程
     * @param courseInfoBean
     */
    @Delete
    void delCourse(CourseInfoBean courseInfoBean);

    /**
     * 批量更新课程信息
     * @param courseInfoBeans 课程信息列表
     * @return
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateCourse(List<CourseInfoBean> courseInfoBeans);
}
