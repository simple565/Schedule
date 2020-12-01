package com.maureen.schedule.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.maureen.schedule.CourseViewModel;
import com.maureen.schedule.R;
import com.maureen.schedule.data.CourseInfoBean;

/**
 * Function: 快速添加课程对话框
 * Created:  2017/10/19
 *
 * @author Lianml
 */
public class AddCourseDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "AddCourseDialog";
    private CourseInfoBean mCourseInfoBean;
    private EditText mCourseNameEdt, mCourseTeacherEdt, mCourseClassroomEdt;
    private CourseViewModel mCourseViewModel;
    private String[] mWeekDays, mCourseIndex, mCoursePeriod;
    private int mOffset = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_dialog_add_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        initView(view);
        mCourseInfoBean = new CourseInfoBean();
        mCourseIndex = getResources().getStringArray(R.array.course_count);
        mCoursePeriod = getResources().getStringArray(R.array.period);
        mWeekDays = getResources().getStringArray(R.array.weekDay);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCourseInfoBean = null;
    }

    /**
     * @param view
     */
    public void initView(View view) {
        mCourseNameEdt = view.findViewById(R.id.add_course_name);
        mCourseTeacherEdt = view.findViewById(R.id.add_course_teacher);
        mCourseClassroomEdt = view.findViewById(R.id.add_course_classroom);
        Spinner courseTimeSp = view.findViewById(R.id.add_course_spinner_period);
        courseTimeSp.setOnItemSelectedListener(this);
        Spinner starTimeSpinner = view.findViewById(R.id.add_course_spinner_start_time);
        starTimeSpinner.setOnItemSelectedListener(this);
        Spinner endTimeSpinner = view.findViewById(R.id.add_course_spinner_end_time);
        endTimeSpinner.setOnItemSelectedListener(this);
        Spinner weekSpinner = view.findViewById(R.id.add_course_spinner_week);
        weekSpinner.setOnItemSelectedListener(this);

        TextView submitTv = view.findViewById(R.id.add_course_tv_submit);
        submitTv.setOnClickListener(v -> {
            saveCourseInfo();
            dismiss();
        });
        TextView cancelTv = view.findViewById(R.id.add_course_tv_cancel);
        cancelTv.setOnClickListener(v -> dismiss());
    }

    private void saveCourseInfo() {
        //处理需要传递的数据
        mCourseInfoBean.setName(mCourseNameEdt.getText().toString().trim());
        mCourseInfoBean.setClassroom(mCourseClassroomEdt.getText().toString().trim());
        mCourseInfoBean.setTeacher(mCourseTeacherEdt.getText().toString().trim());
        mCourseViewModel.saveCourseInfo(mCourseInfoBean);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.add_course_spinner_period) {
            if (position == 1) {
                mOffset = 4;
            } else if (position == 2) {
                mOffset = 7;
            }
        } else if (parent.getId() == R.id.add_course_spinner_week) {
            mCourseInfoBean.setWeekTime(mWeekDays[position]);
        } else if (parent.getId() == R.id.add_course_spinner_start_time) {
            mCourseInfoBean.setBeginTime(Integer.parseInt(mCourseIndex[position]) + mOffset);
        } else if (parent.getId() == R.id.add_course_spinner_end_time) {
            mCourseInfoBean.setLength(Integer.parseInt(mCourseIndex[position]) - mCourseInfoBean.getBeginTime() + 1);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (parent.getId() == R.id.add_course_spinner_period) {
            mOffset = 0;
        } else if (parent.getId() == R.id.add_course_spinner_week) {
            mCourseInfoBean.setWeekTime(mWeekDays[0]);
        } else if (parent.getId() == R.id.add_course_spinner_start_time) {
            mCourseInfoBean.setBeginTime(Integer.parseInt(mCourseIndex[0]));
        } else if (parent.getId() == R.id.add_course_spinner_end_time) {
            mCourseInfoBean.setLength(2);
        }
    }
}

