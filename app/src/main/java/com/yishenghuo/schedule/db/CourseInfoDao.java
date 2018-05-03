package com.yishenghuo.schedule.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yishenghuo.schedule.Model.CourseModel;

import java.util.ArrayList;
import java.util.List;

public class CourseInfoDao {

    private DBHelper dbHelper;
    private SQLiteDatabase db;


    public CourseInfoDao(Context context) {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public boolean isTableExist() {
        boolean result = false;
        Cursor cursor;
        String sql = "select count(*) as c from sqlite_master where type = ? and name = ?";
        cursor = db.rawQuery(sql, new String[]{
                "table", "course_info"
        });
        try {
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0)
                    result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        cursor.close();
        return result;
    }

    //插入课程
    public void insert(CourseModel cInfo) {
        try {
            // 如果找到相同（时间地点相同）课程，提示课程冲突

            // 否则插入课程
            String sql = "INSERT INTO course_info (beginWeek,endWeek,weekType,weekTime,beginTime,length," +
                    "courseName,teacher,classroom) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            db.execSQL(sql, new Object[]{
                    cInfo.getBeginWeek(),
                    cInfo.getEndWeek(),
                    cInfo.getWeekType(),
                    cInfo.getWeekTime(),
                    cInfo.getBeginTime(),
                    cInfo.getLength(),
                    cInfo.getName(),
                    cInfo.getTeacher(),
                    cInfo.getClassroom()
            });
            Log.d("测试", "insert" + cInfo.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //删除课程
    public boolean delete(int cid) {
        try {
            String sql = "DELETE FROM course_info WHERE cid=?";
            db.execSQL(sql, new Object[]{cid});
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //数据库读取课程信息
    public List<CourseModel> getCourseInfo() {
        List<CourseModel> courseModelList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM course_info", null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            String classroom = cursor.getString(2);
            String weektime = cursor.getString(7);
            String teacher = cursor.getString(3);
            int beginweek = cursor.getInt(4);
            int endweek = cursor.getInt(5);
            int weektype = cursor.getInt(6);
            int begintime = cursor.getInt(8);
            int length = cursor.getInt(9);
            CourseModel courseModel = new CourseModel(name, classroom, teacher, weektime, weektype,
                    beginweek, endweek, begintime, length);
            courseModelList.add(courseModel);
        }
        cursor.close();
        return courseModelList;

    }

}
