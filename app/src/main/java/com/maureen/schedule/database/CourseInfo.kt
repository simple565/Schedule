package com.maureen.schedule.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Function:
 *
 * @author lianml
 * Create 2021-10-16
 */
@Entity(tableName = "course")
class CourseInfo {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    /**
     * name 课程名称
     */
    var name: String = ""

    /**
     * classroom 教室
     */
    var classroom: String = ""

    /**
     * teacher 教师
     */
    var teacher: String = ""

    /**
     * weekTime 周X 1-7
     */
    var weekTime: String = ""

    /**
     * weekType 单双周 1-单周 2-双周 0-普通
     */
    var weekType: Int = 0

    /**
     * beginWeek 开课周数
     */
    var beginWeek: Int = 1

    /**
     * endWeek 结课周数
     */
    var endWeek: Int = 0

    /**
     * beginTime 上课时间
     */
    var beginTime: Int = 1

    /**
     * length 节数
     */
    var length: Int = 1
}