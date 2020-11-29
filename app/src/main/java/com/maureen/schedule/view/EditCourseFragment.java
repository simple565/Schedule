package com.maureen.schedule.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.maureen.schedule.CourseViewModel;
import com.maureen.schedule.R;
import com.maureen.schedule.data.CourseInfoBean;

/**
 * Function:
 * Create:   2020/11/27
 *
 * @author lianml
 */
public class EditCourseFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {
    private CourseViewModel mCourseViewModel;
    private CourseInfoBean mCourseInfoBean;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        RadioGroup weekTypeRgb = view.findViewById(R.id.add_course_rgb_week_type);
        weekTypeRgb.setOnCheckedChangeListener(this);
        /*Spinner startWeekSpinner = view.findViewById(R.id.add_course_spinner_start_week);
        startWeekSpinner.setOnItemSelectedListener(this);
        Spinner endWeekSpinner = view.findViewById(R.id.add_course_spinner_end_week);
        endWeekSpinner.setOnItemSelectedListener(this);*/
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.add_course_spinner_period) {

        } else if (parent.getId() == R.id.add_course_spinner_week) {

        } else if (parent.getId() == R.id.add_course_spinner_start_time) {

        } else if (parent.getId() == R.id.add_course_spinner_end_time) {

        } /*else if (parent.getId() == R.id.add_course_spinner_start_week) {

        } else if (parent.getId() == R.id.add_course_spinner_end_week) {

        }*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (parent.getId() == R.id.add_course_spinner_period) {

        } else if (parent.getId() == R.id.add_course_spinner_week) {

        } else if (parent.getId() == R.id.add_course_spinner_start_time) {

        } else if (parent.getId() == R.id.add_course_spinner_end_time) {

        } /*else if (parent.getId() == R.id.add_course_spinner_start_week) {

        } else if (parent.getId() == R.id.add_course_spinner_end_week) {

        }*/
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
