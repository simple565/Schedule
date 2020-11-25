package com.maureen.schedule.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.maureen.schedule.R;
import com.maureen.schedule.data.CourseInfoBean;
import com.maureen.schedule.utils.TimeUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<CourseInfoBean> mCourseInfoBeanList = null;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
        initData();
    }

    protected void initView() {
        TextView monthTv = (TextView) findViewById(R.id.main_tv_month);
        monthTv.setText(TimeUtil.getCurrentMonth());
        ImageButton settingsBtn = (ImageButton) findViewById(R.id.home_setting);
        settingsBtn.setOnClickListener(v -> {
        });
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.main_fab);
        LinearLayout dateLayout = findViewById(R.id.main_ll_week);
        List<TimeUtil.WeekDay> weekDays = TimeUtil.getDayOfCurrentWeek();
        for (int i = 0; i < 7; i++) {
            TimeUtil.WeekDay day = weekDays.get(i);
            TextView view = new TextView(mContext);
            view.setBackgroundResource(R.color.dividerColor);
            view.setGravity(Gravity.CENTER);
            view.setText(String.format("%s\n%s", day.getWeekIndex(), day.getDate()));
            dateLayout.addView(view);
        }
    }

    private void initData() {

    }
}