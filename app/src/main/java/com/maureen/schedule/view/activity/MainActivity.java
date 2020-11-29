package com.maureen.schedule.view.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.maureen.schedule.CourseViewModel;
import com.maureen.schedule.R;
import com.maureen.schedule.data.CourseInfoBean;
import com.maureen.schedule.utils.DateUtil;
import com.maureen.schedule.utils.DisplayUtil;
import com.maureen.schedule.utils.InfoUtil;
import com.maureen.schedule.view.AddCourseDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
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
                view.setBackgroundResource(R.drawable.course_bg_blue);
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
        List<CourseInfoBean> list = new ArrayList<>();

        CourseInfoBean courseInfoBean = new CourseInfoBean();
        courseInfoBean.setName("测试数据");
        courseInfoBean.setLength(2);
        courseInfoBean.setBeginTime(8);
        courseInfoBean.setWeekTime("周三");
        list.add(courseInfoBean);

        GridLayout gridLayout = findViewById(R.id.main_gl_table);
        gridLayout.setColumnCount(7);
        gridLayout.setRowCount(9);
        for (int i = 1; i <= 7; i++) {
            for (int j = 1; j <= 9; j++) {
                TextView courseInfoView = new TextView(mContext);
                courseInfoView.setBackgroundResource(R.color.white);
                GridLayout.Spec rowSpec = GridLayout.spec(j - 1, 1f);
                GridLayout.Spec columnSpec = GridLayout.spec(i - 1, 1f);
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
                layoutParams.height = 0;
                layoutParams.width = 0;
                layoutParams.setGravity(Gravity.FILL);
                gridLayout.addView(courseInfoView, layoutParams);
            }
        }
        for (CourseInfoBean courseInfo : list) {
            gridLayout.addView(genCourseItemView(courseInfo));
        }
    }

    private void initData() {
        mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        mWeekIndex = "第一周";
        mMonthIndex = DateUtil.getCurrentMonth();
        mCourseViewModel.getCourseInfoLiveData().observe(this, courseInfoBeans -> {
            Log.d(TAG, "initData: " + courseInfoBeans.size());
        });
    }

    private TextView genCourseItemView(CourseInfoBean courseInfo) {
        TextView courseInfoView = new TextView(mContext);
        courseInfoView.setGravity(Gravity.CENTER);
        courseInfoView.setText(courseInfo.getName());
        courseInfoView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        courseInfoView.setBackgroundResource(R.drawable.course_bg_brown);
        GridLayout.Spec rowSpec = GridLayout.spec(courseInfo.getBeginTime(), 1f * courseInfo.getLength());
        GridLayout.Spec columnSpec = GridLayout.spec(InfoUtil.getWeekDay(mContext, courseInfo.getWeekTime()), 1f);
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
        layoutParams.height = 0;
        layoutParams.width = 0;
        layoutParams.setGravity(Gravity.FILL);
        courseInfoView.setLayoutParams(layoutParams);
        courseInfoView.setOnClickListener(v -> jumpToEditCourseInfoFragment(courseInfo));
        courseInfoView.setOnLongClickListener(v -> {
            showDeleteConfirmDialog(courseInfo);
            return false;
        });
        return courseInfoView;
    }

    private void jumpToEditCourseInfoFragment(CourseInfoBean courseInfo) {
        Log.d(TAG, "jumpToEditCourseInfoFragment: " + courseInfo.getName());
    }

    private void showDeleteConfirmDialog(CourseInfoBean courseInfo) {
        Log.d(TAG, "showDeleteConfirmDialog: " + courseInfo.getName());
    }

    private void showAddCourseDialog() {
        if (null == mAddCourseDialog) {
            mAddCourseDialog = new AddCourseDialog();
            mAddCourseDialog.setCancelable(false);
        }
        mAddCourseDialog.show(getSupportFragmentManager(), AddCourseDialog.class.getSimpleName());
    }
}