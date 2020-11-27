package com.maureen.schedule.data;

import java.util.List;

import androidx.lifecycle.LiveData;
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
    LiveData<List<CourseInfoBean>> getAllCourses();

    /**
     * 查找指定id的课程信息
     *
     * @param id 课程id
     * @return 课程信息
     */
    @Query("select * from course where id = :id")
    CourseInfoBean getCourseInfoById(int id);

    /**
     * 插入单条课程数据
     *
     * @param course 课程信息
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveCourseInfo(CourseInfoBean course);

    /**
     * 根据名称删除指定课程
     *
     * @param id 课程id
     */
    @Query("delete from course where name = :id")
    void delCourseById(int id);

    /**
     * 删除指定课程
     *
     * @param courseInfoBean
     */
    @Delete
    void delCourse(CourseInfoBean courseInfoBean);

    /**
     * 批量更新课程信息
     *
     * @param courseInfoBean 课程信息列表
     * @return
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateCourse(CourseInfoBean courseInfoBean);
}
