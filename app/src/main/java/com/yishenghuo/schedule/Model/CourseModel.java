package com.yishenghuo.schedule.Model;

/**
 * Created by ihome on 2017/10/18.
 */

/**
 * name 课程名称
 * classroom 教室
 * teacher 教师
 * weekTime 周X
 * weekType 单双周 1-单周 2-双周 0-普通
 * beginWeek 开课周数
 * endWeek 结课周数
 * beginTime 上课时间
 * length 节数
 */
public class CourseModel {
    private String name;
    private String classroom;
    private String teacher;
    private String weekTime;
    private int weekType;
    private int beginWeek;
    private int endWeek;
    private int beginTime;
    private int length;

    public CourseModel(String name, String classroom, String teacher, String weekTime,
                       int weekType, int beginWeek, int endWeek, int beginTime, int length) {
        this.name = name;
        this.classroom = classroom;
        this.teacher = teacher;
        this.weekTime = weekTime;
        this.weekType = weekType;
        this.beginWeek = beginWeek;
        this.endWeek = endWeek;
        this.beginTime = beginTime;
        this.length = length;
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

