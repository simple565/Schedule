package com.yishenghuo.schedule.UI;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.yishenghuo.schedule.R;
import com.yishenghuo.schedule.SendInfo;

/**
 * Created by Lian on 2017/10/19.
 */

public class AddCourseDialog extends DialogFragment implements TextView.OnEditorActionListener {
    //dialog中数据，需传递给activity
    private String name;
    private String classroom;
    private String teacher;
    private String weekTime;
    private String period;
    private int weekType = 0;//默认为普通
    private int beginWeek;
    private int endWeek;
    private int beginTime;
    private int endTime;

    private View view;
    private EditText edt_name, edt_teacher, edt_classroom;
    private Button btn_cancel, btn_submit;
    private Spinner spinner_week, spinner_startweek, spinner_endweek;
    private Spinner spinner_time, spinner_startime, spinner_endtime;
    private RadioGroup mradioGroup;

    private RadioButton mrdb_odd, mrdb_even;

    private SendInfo sendInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(Window.FEATURE_NO_TITLE, R.style.Theme_AppCompat_Light_Dialog_MinWidth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialogfragment_addcourse, container);
        iniView();
        return view;
    }

    public void iniView() {
        edt_name = (EditText) view.findViewById(R.id.add_course_name);
        edt_teacher = (EditText) view.findViewById(R.id.add_course_teacher);
        edt_classroom = (EditText) view.findViewById(R.id.add_course_classroom);
        spinner_time = (Spinner) view.findViewById(R.id.add_course_spinner_period);
        spinner_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                period = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                period = parent.getItemAtPosition(0).toString();
            }
        });

        spinner_startime = (Spinner) view.findViewById(R.id.add_course_spinner_start_time);
        spinner_startime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                beginTime = Integer.parseInt(spinner_startime.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                beginTime = Integer.parseInt(spinner_startime.getItemAtPosition(0).toString());
            }
        });

        spinner_endtime = (Spinner) view.findViewById(R.id.add_course_spinner_end_time);
        spinner_endtime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                endTime = Integer.parseInt(spinner_endtime.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                endTime = Integer.parseInt(spinner_endtime.getItemAtPosition(0).toString());
            }
        });

        spinner_week = (Spinner) view.findViewById(R.id.add_course_spinner_week);
        spinner_week.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                weekTime = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                weekTime = spinner_week.getItemAtPosition(0).toString();
            }
        });

        spinner_startweek = (Spinner) view.findViewById(R.id.add_course_spinner_start_week);
        spinner_startweek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                beginWeek = Integer.parseInt(spinner_startweek.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                beginWeek = Integer.parseInt(spinner_startweek.getItemAtPosition(0).toString());
            }
        });

        spinner_endweek = (Spinner) view.findViewById(R.id.add_course_spinner_end_week);
        spinner_endweek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                endWeek = Integer.parseInt(spinner_endweek.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                endWeek = Integer.parseInt(spinner_endweek.getItemAtPosition(0).toString());
            }
        });

        mradioGroup = (RadioGroup) view.findViewById(R.id.add_course_radiogroup_type);
        mrdb_even = (RadioButton) view.findViewById(R.id.add_course_radio_even);
        mrdb_odd = (RadioButton) view.findViewById(R.id.add_course_radio_odd);

        mradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.add_course_radio_odd)
                    weekType = 1;//单周
                if (checkedId == R.id.add_course_radio_even)
                    weekType = 2;//双周
            }
        });


        btn_submit = (Button) view.findViewById(R.id.add_course_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //处理需要传递的数据
                name = edt_name.getText().toString();
                classroom = edt_classroom.getText().toString();
                teacher = edt_teacher.getText().toString();

                sendInfo = (SendInfo) getActivity();//?????????
                sendInfo.onSendDialogInfo(name, classroom, teacher, weekTime, period, weekType,
                        beginWeek, endWeek, beginTime, endTime);
                dismiss();
            }
        });
        btn_cancel = (Button) view.findViewById(R.id.add_course_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }



    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }


}

