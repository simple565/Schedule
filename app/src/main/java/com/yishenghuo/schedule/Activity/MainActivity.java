package com.yishenghuo.schedule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yishenghuo.schedule.Model.CourseModel;
import com.yishenghuo.schedule.R;
import com.yishenghuo.schedule.SendInfo;
import com.yishenghuo.schedule.UI.AddCourseDialog;
import com.yishenghuo.schedule.Util.InfoUtil;
import com.yishenghuo.schedule.Util.TimeUtil;
import com.yishenghuo.schedule.db.CourseInfoDao;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SendInfo {
    private AddCourseDialog addCourseDialog;
    private TextView mtv_date, mtv_week_count;
    private ImageButton igb_setting;
    private RelativeLayout relativeLayout;
    private int course_width;
    private int course_height;
    private List<CourseModel> courseModelList = new ArrayList<>();
    private int[] COLOR = {R.drawable.course_bg_blue, R.drawable.course_bg_brown,
            R.drawable.course_bg_cyan, R.drawable.course_bg_green,
            R.drawable.course_bg_orange, R.drawable.course_bg_pink,
            R.drawable.course_bg_purple, R.drawable.course_bg_yellow
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniView();
        iniCourseCard();
    }

    protected void iniView() {
        mtv_date = (TextView) findViewById(R.id.tv_date);
        mtv_date.setText(TimeUtil.getDate());
        mtv_week_count = (TextView) findViewById(R.id.home_week_count);
        igb_setting = (ImageButton) findViewById(R.id.home_setting);
        igb_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_setting = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent_setting);
            }
        });
        relativeLayout = (RelativeLayout) findViewById(R.id.table);
        addCourseDialog = new AddCourseDialog();
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourseDialog.show(getFragmentManager(), "addCourseDialog");
            }
        });


    }

    //初始化日期，获得当前时间，周数
    protected void iniDate() {

    }

    protected void iniCourseCard() {
        CourseInfoDao courseInfoDao = new CourseInfoDao(this);
        if (courseInfoDao.isTableExist()) {
            courseModelList = courseInfoDao.getCourseInfo();
            int nowWeek = InfoUtil.getNowWeek(mtv_week_count.getText().toString());
            for (int i = 0; i < courseModelList.size(); i++) {
                int beginWeek = courseModelList.get(i).getBeginWeek();
                int endWeek = courseModelList.get(i).getEndWeek();
                int weekType = courseModelList.get(i).getWeekType();
                createCard(courseModelList.get(i),
                        InfoUtil.isOnWeek(beginWeek, endWeek, nowWeek, weekType));
                Log.d("测试", courseModelList.get(i).getName() + courseModelList.get(i).getWeekType()+nowWeek);
            }

        }

    }

    /**
     * 获取单个课表宽度
     * 获取节数表单格长度
     */
    protected int getPara() {
        float value = getResources().getDisplayMetrics().density;
        course_height = getResources().getDimensionPixelSize(R.dimen.count_height) + (int) (value + 0.5f);
        int count_width = getResources().getDimensionPixelSize(R.dimen.count_width);
        int screen_width = getResources().getDisplayMetrics().widthPixels;
        course_width = (screen_width - count_width) / 7;
        return (int) (Math.random() * 8);
    }

    public int getCourseCardX(String string) {
        int margin_left = 0;
        if (string.equals("周一"))
            margin_left = 0;
        if (string.equals("周二"))
            margin_left = 1;
        if (string.equals("周三"))
            margin_left = 2;
        if (string.equals("周四"))
            margin_left = 3;
        if (string.equals("周五"))
            margin_left = 4;
        if (string.equals("周六"))
            margin_left = 5;
        if (string.equals("周日"))
            margin_left = 6;
        return margin_left;
    }

    /**
     * 生成课程卡片
     */
    protected void createCard(CourseModel courseModel, Boolean flag) {
        int number;//随机数，随机取颜色数组
        number = getPara();
        String name = courseModel.getName();
        String classroom = courseModel.getClassroom();
        String weekTime = courseModel.getWeekTime();
        int beginTime = courseModel.getBeginTime();
        int length = courseModel.getLength();

        //根据结束生成相应长度的格子
        int margin_top = course_height * (beginTime - 1);
        course_height = course_height * length;
        int margin_left = course_width * getCourseCardX(weekTime);
        //new textview设置相关属性
        TextView tv = new TextView(this);
        tv.setText(name + "@" + classroom);
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundResource(COLOR[number]);
        //课程在当前周数未开课或已结课则不显示
        if (!flag)
            tv.setVisibility(TextView.GONE);


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                course_width - 5, course_height - 5);
        layoutParams.setMargins(margin_left, margin_top, 0, 0);//left,top

        tv.setLayoutParams(layoutParams);
        tv.setOnClickListener(new View.OnClickListener() {//点击显示详情
            @Override
            public void onClick(View v) {
                //Log.d("测试", );
            }
        });
        relativeLayout.addView(tv);
    }


    /**
     * 保存数据到数据库
     *
     * @param courseModel
     */
    protected void safeCourseInfo(CourseModel courseModel) {
        CourseInfoDao courseInfoDao = new CourseInfoDao(this);
        courseInfoDao.insert(courseModel);
        Log.d("测试", "插入数据");
    }

    @Override
    public void onSendDialogInfo(String name, String classroom, String teacher, String weekTime, String period,
                                 int weekType, int beginWeek, int endWeek, int beginTime, int endTime) {
        int length = InfoUtil.getLength(beginTime, endTime);
        beginTime = InfoUtil.getBeginTime(period, beginTime);
        endTime = InfoUtil.getEndTime(period, endTime);
        //判断填写信息符合标准后保存课程信息到数据库
        if (InfoUtil.isLegal(beginTime, endTime, beginWeek, endWeek).equals(" ")) {
            CourseModel courseModel = new CourseModel(name, classroom, teacher, weekTime, weekType,
                    beginWeek, endWeek, beginTime, length);
            courseModelList.add(courseModel);
            safeCourseInfo(courseModel);
            createCard(courseModel, true);
        } else
            Toast.makeText(this, InfoUtil.isLegal(beginTime, endTime, beginWeek, endWeek),
                    Toast.LENGTH_SHORT).show();
    }
}