package com.maureen.schedule.view.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.maureen.schedule.R;
import com.maureen.schedule.data.CourseInfoBean;
import com.maureen.schedule.utils.InfoUtil;
import com.maureen.schedule.utils.TimeUtil;
import com.maureen.schedule.view.AddCourseDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private AddCourseDialog addCourseDialog;
    //课表内容布局
    private RelativeLayout courseTable;
    //课表宽度
    private int course_width;
    //课表高度
    private int course_height;
    private List<CourseInfoBean> mCourseInfoBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniView();
        iniCourseCard();
    }

    protected void iniView() {
        //日期
        TextView dateTv = (TextView) findViewById(R.id.main_tv_month);
        dateTv.setText(TimeUtil.getCurrentMonth());
        //设置
        ImageButton settingsBtn = (ImageButton) findViewById(R.id.home_setting);
        settingsBtn.setOnClickListener(v -> {
        });


        //悬浮菜单
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.main_fab);
        addCourseDialog = new AddCourseDialog();
        floatingActionButton.setOnClickListener(v -> addCourseDialog.show(getSupportFragmentManager(), "addCourseDialog"));
        courseTable = (RelativeLayout) findViewById(R.id.table);

    }

    /**
     * 初始化日期，获得当前时间，周数
     */
    protected void iniDate() {

    }

    protected void iniCourseCard() {
       /* CourseInfoDao courseInfoDao = new CourseInfoDao(this);
        if (courseInfoDao.isTableExist()) {
            mCourseInfoBeanList = courseInfoDao.getCourseInfo();
            //mtv_week_count.getText ().toString ()
            int nowWeek = InfoUtil.getNowWeek("6");
            for (int i = 0; i < mCourseInfoBeanList.size(); i++) {
                int beginWeek = mCourseInfoBeanList.get(i).getBeginWeek();
                int endWeek = mCourseInfoBeanList.get(i).getEndWeek();
                int weekType = mCourseInfoBeanList.get(i).getWeekType();
                createCard(mCourseInfoBeanList.get(i),
                        InfoUtil.isOnWeek(beginWeek, endWeek, nowWeek, weekType));
            }
        }*/
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
        if (string.equals("周一")) {
            margin_left = 0;
        }
        if (string.equals("周二")) {
            margin_left = 1;
        }
        if (string.equals("周三")) {
            margin_left = 2;
        }
        if (string.equals("周四")) {
            margin_left = 3;
        }
        if (string.equals("周五")) {
            margin_left = 4;
        }
        if (string.equals("周六")) {
            margin_left = 5;
        }
        if (string.equals("周日")) {
            margin_left = 6;
        }
        return margin_left;
    }

    /**
     * 生成课程卡片
     */
    protected void createCard(CourseInfoBean courseInfoBean, Boolean flag) {
        int number;//随机数，随机取颜色数组
        number = getPara();
        String name = courseInfoBean.getName();
        String classroom = courseInfoBean.getClassroom();
        String weekTime = courseInfoBean.getWeekTime();
        int beginTime = courseInfoBean.getBeginTime();
        int length = courseInfoBean.getLength();

        //根据结束生成相应长度的格子
        int margin_top = course_height * (beginTime - 1);
        course_height = course_height * length;
        int margin_left = course_width * getCourseCardX(weekTime);
        //new textview设置相关属性
        TextView tv = new TextView(this);
        tv.setText(name + "@" + classroom);
        tv.setGravity(Gravity.CENTER);
        //tv.setBackgroundResource(COLOR[number]);
        //课程在当前周数未开课或已结课则不显示
        if (!flag) {
            tv.setVisibility(TextView.GONE);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(course_width - 5, course_height - 5);
        layoutParams.setMargins(margin_left, margin_top, 0, 0);
        tv.setLayoutParams(layoutParams);
        //点击显示详情
        tv.setOnClickListener(v -> courseTable.removeView(tv));
        courseTable.addView(tv);
    }


    /**
     * 保存数据到数据库
     *
     * @param courseInfoBean 课程信息模型
     */
    protected void safeCourseInfo(CourseInfoBean courseInfoBean) {

    }


    public void onSendDialogInfo(String name, String classroom, String teacher, String weekTime, String period,
                                 int weekType, int beginWeek, int endWeek, int beginTime, int endTime) {
        int length = InfoUtil.getLength(beginTime, endTime);
        beginTime = InfoUtil.getBeginTime(period, beginTime);
        endTime = InfoUtil.getEndTime(period, endTime);
        //判断填写信息符合标准后保存课程信息到数据库
        if (InfoUtil.isLegal(beginTime, endTime, beginWeek, endWeek).equals(" ")) {
            CourseInfoBean courseInfoBean = new CourseInfoBean();
            mCourseInfoBeanList.add(courseInfoBean);
            safeCourseInfo(courseInfoBean);
            createCard(courseInfoBean, true);
        } else {
            Toast.makeText(this, InfoUtil.isLegal(beginTime, endTime, beginWeek, endWeek), Toast.LENGTH_SHORT).show();
        }
    }
}