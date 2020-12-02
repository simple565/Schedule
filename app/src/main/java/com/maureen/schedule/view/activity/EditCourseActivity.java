package com.maureen.schedule.view.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioGroup;

import com.maureen.schedule.CourseViewModel;
import com.maureen.schedule.R;
import com.maureen.schedule.data.CourseInfoBean;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

/**
 * Function:
 * Create:   2020/11/27
 *
 * @author lianml
 */
public class EditCourseActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private CourseViewModel mCourseViewModel;
    private CourseInfoBean mCourseInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.fragment_edit_course);
        initData();
        initView();
    }

    private void initData() {
        mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        if (null != getIntent()) {
            mCourseInfoBean = mCourseViewModel.findCourseById(getIntent().getIntExtra("CourseId", 0));
        } else {
            mCourseInfoBean = new CourseInfoBean();
        }
    }

    private void initView() {
        RadioGroup weekTypeRgb = findViewById(R.id.add_course_rgb_week_type);
        weekTypeRgb.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.add_course_radio_odd) {
            //单周
            mCourseInfoBean.setWeekType(1);
        } else if (checkedId == R.id.add_course_radio_even) {
            //双周
            mCourseInfoBean.setWeekType(2);
        }
    }
}
