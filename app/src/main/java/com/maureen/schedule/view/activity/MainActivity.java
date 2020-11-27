package com.maureen.schedule.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.maureen.schedule.CourseViewModel;
import com.maureen.schedule.R;
import com.maureen.schedule.data.CourseInfoBean;
import com.maureen.schedule.utils.DateUtil;
import com.maureen.schedule.utils.DisplayUtil;
import com.maureen.schedule.view.AddCourseDialog;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private List<CourseInfoBean> mCourseInfoBeanList = null;
    private AddCourseDialog mAddCourseDialog;
    private TextView mCurWeekIndexTv;
    private TextView mMonthTv;
    private String mWeekIndex, mMonthIndex;
    private CourseViewModel mCourseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initData();
        initView();
    }

    private void initView() {
        mCurWeekIndexTv = findViewById(R.id.main_tv_current_week);
        mMonthTv = findViewById(R.id.main_tv_month);
        mCurWeekIndexTv.setText(mWeekIndex);
        mMonthTv.setText(mMonthIndex);
        ImageButton settingsBtn = findViewById(R.id.home_setting);
        settingsBtn.setOnClickListener(v -> showAddCourseDialog());
        FloatingActionButton floatingActionButton = findViewById(R.id.main_fab);
        floatingActionButton.setOnClickListener(v -> showAddCourseDialog());
        // 初始化日期栏
        int weekDayViewWidth = (DisplayUtil.getScreenWidth(mContext) - DisplayUtil.dp2px(mContext, 32)) / 7;
        LinearLayout dateLayout = findViewById(R.id.main_ll_week);
        List<DateUtil.WeekDay> weekDays = DateUtil.getDayOfCurrentWeek();
        for (int i = 0; i < 7; i++) {
            DateUtil.WeekDay day = weekDays.get(i);
            TextView view = new TextView(mContext);
            view.setWidth(weekDayViewWidth);
            view.setGravity(Gravity.CENTER);
            view.setText(String.format("%s\n%s", day.getWeekIndex(), day.getDate()));
            if (day.isCurrent()) {
                view.setBackgroundResource(R.color.colorAccent);
                view.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            }
            dateLayout.addView(view);
        }

        // 初始化节数栏
        LinearLayout countLayout = findViewById(R.id.main_ll_course_count);
        for (int i = 0; i < 10; i++) {
            TextView view = new TextView(mContext);
            view.setHeight(DisplayUtil.dp2px(mContext, 64));
            view.setGravity(Gravity.CENTER);
            view.setText(String.valueOf(i + 1));
            countLayout.addView(view);
        }
    }

    private void initData() {
        mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        mWeekIndex = "第一周";
        mMonthIndex = DateUtil.getCurrentMonth();
        mCourseViewModel.getCourseInfoLiveData().observe(this, courseInfoBeans -> {
            Log.d("lianml", "initData: " + courseInfoBeans.size());
        });
    }

    private void showAddCourseDialog() {
        if (null == mAddCourseDialog) {
            mAddCourseDialog = new AddCourseDialog();
        }
        mAddCourseDialog.show(getSupportFragmentManager(), AddCourseDialog.class.getSimpleName());
    }
}