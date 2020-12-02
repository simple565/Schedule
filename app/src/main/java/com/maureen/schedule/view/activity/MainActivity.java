package com.maureen.schedule.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maureen.schedule.CourseViewModel;
import com.maureen.schedule.R;
import com.maureen.schedule.data.CourseInfoBean;
import com.maureen.schedule.utils.DateUtil;
import com.maureen.schedule.utils.DisplayUtil;
import com.maureen.schedule.utils.InfoUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

/**
 * @author lianml
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Context mContext;
    private List<TextView> mCourseItemViews;
    private TextView mCurWeekIndexTv;
    private TextView mMonthTv;
    private String mWeekIndex, mMonthIndex;
    private CourseViewModel mCourseViewModel;
    private GridLayout mTableLayout;
    private int mWeekLength = 7;
    private int mTotalCourCount = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
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
        // 初始化日期栏
        int weekDayViewWidth = (DisplayUtil.getScreenWidth(mContext) - DisplayUtil.dp2px(mContext, 32)) / 7;
        LinearLayout dateLayout = findViewById(R.id.main_ll_week);
        List<DateUtil.WeekDay> weekDays = DateUtil.getDayOfCurrentWeek(mWeekLength);
        for (int i = 0; i < mWeekLength; i++) {
            DateUtil.WeekDay day = weekDays.get(i);
            TextView view = new TextView(mContext);
            view.setWidth(weekDayViewWidth);
            view.setGravity(Gravity.CENTER);
            view.setText(String.format("%s\n%s", day.getWeekIndex(), day.getDate()));
            if (day.isCurrent()) {
                view.setBackgroundResource(R.drawable.course_bg_blue);
                view.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            }
            dateLayout.addView(view);
        }

        // 初始化节数栏
        LinearLayout countLayout = findViewById(R.id.main_ll_course_count);
        for (int i = 0; i < mTotalCourCount; i++) {
            TextView view = new TextView(mContext);
            view.setHeight(DisplayUtil.dp2px(mContext, 64));
            view.setGravity(Gravity.CENTER);
            view.setText(String.valueOf(i + 1));
            countLayout.addView(view);
        }

        mTableLayout = findViewById(R.id.main_gl_table);
        mTableLayout.setColumnCount(mWeekLength);
        mTableLayout.setRowCount(mTotalCourCount);
        initTableView();
    }

    private void initData() {
        mCourseItemViews = new ArrayList<>();
        mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        mWeekIndex = "第一周";
        mMonthIndex = DateUtil.getCurrentMonth();
        mCourseViewModel.getCourseInfoLiveData().observe(this, courseInfoBeans -> {
            if (courseInfoBeans.size() > 0) {
                mTableLayout.addView(genCourseItemView(courseInfoBeans.get(courseInfoBeans.size() - 1)));
            }

        });
    }

    private void initTableView() {
        for (int i = 0; i < mWeekLength; i++) {
            for (int j = 0; j < mTotalCourCount; j++) {
                TextView courseInfoView = new TextView(mContext);
                courseInfoView.setBackgroundResource(R.color.white);
                GridLayout.Spec columnSpec = GridLayout.spec(i, 1f);
                GridLayout.Spec rowSpec = GridLayout.spec(j, 1f);
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    layoutParams.height = 0;
                    layoutParams.width = 0;
                } else {
                    layoutParams.width = 0;
                    layoutParams.height = DisplayUtil.dp2px(mContext, 64);
                }
                layoutParams.setGravity(Gravity.FILL);
                courseInfoView.setLayoutParams(layoutParams);
                courseInfoView.setOnClickListener(v -> jumpToEditCourseInfo(null));
                mTableLayout.addView(courseInfoView);
            }
        }
    }

    private TextView genCourseItemView(CourseInfoBean courseInfo) {
        TextView courseInfoView = new TextView(mContext);
        courseInfoView.setGravity(Gravity.CENTER);
        courseInfoView.setText(courseInfo.getName());
        courseInfoView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        courseInfoView.setBackgroundResource(R.drawable.course_bg_cyan);
        GridLayout.Spec rowSpec = GridLayout.spec(courseInfo.getBeginTime() - 1, courseInfo.getLength(), 1f);
        GridLayout.Spec columnSpec = GridLayout.spec(InfoUtil.getWeekDay(mContext, courseInfo.getWeekTime()), 1, 1f);
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
        layoutParams.height = 0;
        layoutParams.width = 0;
        layoutParams.setGravity(Gravity.FILL);
        courseInfoView.setLayoutParams(layoutParams);
        courseInfoView.setOnClickListener(v -> jumpToEditCourseInfo(courseInfo));
        mCourseItemViews.add(courseInfoView);
        return courseInfoView;
    }

    private void jumpToEditCourseInfo(CourseInfoBean courseInfo) {
        Intent intent = new Intent(MainActivity.this, EditCourseActivity.class);
        if (null != courseInfo) {
            intent.putExtra("CourseId", courseInfo.getId());
        }
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCourseViewModel.getCourseInfoLiveData().removeObservers(this);
    }
}