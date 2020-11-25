package com.maureen.schedule.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Function:
 * Create 2017/10/18.
 *
 * @author lianml
 */
@Entity(tableName = "course")
public class CourseInfoBean {


    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * name 课程名称
     */
    private String name;
    /**
     * classroom 教室
     */
    private String classroom;
    /**
     * teacher 教师
     */
    private String teacher;
    /**
     * weekTime 周X
     */
    private String weekTime;
    /**
     * weekType 单双周 1-单周 2-双周 0-普通
     */
    private int weekType = 0;
    /**
     * beginWeek 开课周数
     */
    private int beginWeek;
    /**
     * endWeek 结课周数
     */
    private int endWeek;
    /**
     * beginTime 上课时间
     */
    private int beginTime;
    /**
     * length 节数
     */
    private int length;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getWeekTime() {
        return weekTime;
    }

    public void setWeekTime(String weekTime) {
        this.weekTime = weekTime;
    }

    public int getWeekType() {
        return weekType;
    }

    public void setWeekType(int weekType) {
        this.weekType = weekType;
    }

    public int getBeginWeek() {
        return beginWeek;
    }

    public void setBeginWeek(int beginWeek) {
        this.beginWeek = beginWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public int getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(int beginTime) {
        this.beginTime = beginTime;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }


}

