package com.maureen.schedule.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.maureen.schedule.R;
import com.maureen.schedule.data.CourseInfoBean;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * @author Lianml
 * Created on 2017/10/19
 */
public class AddCourseDialog extends DialogFragment implements RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "AddCourseDialog";
    private CourseInfoBean mCourseInfoBean;
    private EditText mCourseNameEdt, mCourseTeacherEdt, mCourseClassroomEdt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_add_course, container, false);
        initView(view);
        return view;
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
        Spinner startWeekSpinner = view.findViewById(R.id.add_course_spinner_start_week);
        startWeekSpinner.setOnItemSelectedListener(this);
        Spinner endWeekSpinner = view.findViewById(R.id.add_course_spinner_end_week);
        endWeekSpinner.setOnItemSelectedListener(this);
        RadioGroup weekTypeRgb = view.findViewById(R.id.add_course_rgb_week_type);
        weekTypeRgb.setOnCheckedChangeListener(this);
        TextView submitTv = view.findViewById(R.id.add_course_tv_submit);
        submitTv.setOnClickListener(v -> {
            saveCourseInfo();
            dismiss();
        });
        TextView cancelTv = view.findViewById(R.id.add_course_tv_cancel);
        cancelTv.setOnClickListener(v -> dismiss());
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

    private void saveCourseInfo() {
        //处理需要传递的数据
        mCourseInfoBean.setName(mCourseNameEdt.getText().toString().trim());
        mCourseInfoBean.setClassroom(mCourseClassroomEdt.getText().toString().trim());
        mCourseInfoBean.setTeacher(mCourseTeacherEdt.getText().toString().trim());
        /*sendInfo.onSendDialogInfo(name, classroom, teacher, weekTime, period, weekType,
                beginWeek, endWeek, beginTime, endTime);*/
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemSelected: " + parent.getId());
        if (parent.getId() == R.id.add_course_spinner_period) {

        } else if (parent.getId() == R.id.add_course_spinner_week) {

        } else if (parent.getId() == R.id.add_course_spinner_start_time) {

        } else if (parent.getId() == R.id.add_course_spinner_end_time) {

        } else if (parent.getId() == R.id.add_course_spinner_start_week) {

        } else if (parent.getId() == R.id.add_course_spinner_end_week) {

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "onItemSelected: " + parent.getId());
        if (parent.getId() == R.id.add_course_spinner_period) {

        } else if (parent.getId() == R.id.add_course_spinner_week) {

        } else if (parent.getId() == R.id.add_course_spinner_start_time) {

        } else if (parent.getId() == R.id.add_course_spinner_end_time) {

        } else if (parent.getId() == R.id.add_course_spinner_start_week) {

        } else if (parent.getId() == R.id.add_course_spinner_end_week) {

        }
    }
}

