package com.yishenghuo.schedule.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "schedule.db";//数据库名称
    private SQLiteDatabase db;
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL("CREATE TABLE IF NOT EXISTS course_info" +        // 课程信息表
                "(_cid INTEGER primary key autoincrement, " +        // 课程ID,自增长
                "courseName TEXT, " +      // 课程名-1
                "classroom TEXT, " +       // 地点-2
                "teacher TEXT, " +         // 教师-3
                "beginWeek INTEGER, " +     // 开始周次-4
                "endWeek INTEGER, " +       // 结束周次-5
                "weekType INTEGER, " +     // 周次类型-6
                "weekTime TEXT, " +      // 星期几-7
                "beginTime INTEGER, " +   // 节次开始-8
                "length INTEGER )"        // 节次数-9
        );
        Log.d("测试", "创建表单");
    }



    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

