package com.maureen.schedule.view.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.maureen.schedule.CourseViewModel;
import com.maureen.schedule.R;
import com.maureen.schedule.data.CourseInfoBean;
import com.maureen.schedule.utils.DisplayUtil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

/**
 * Function:
 * Create:   2020/11/27
 *
 * @author lianml
 */
public class EditCourseActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "EditCourseActivity";
    private CourseViewModel mCourseViewModel;
    private CourseInfoBean mCourseInfoBean;
    private EditText mCourseNameEdt, mClassroomEdt, mTeacherNameEdt, mCourseLengthEdt;
    private EditText mStartWeekIndexEdt, mEndWeekIndexEdt, mStartClassIndex;
    private boolean isEditMode;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.fragment_edit_course);
        mContext = this;
        initData();
        initView();
    }

    private void initData() {
        mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        isEditMode = false;
        if (null != getIntent()) {
            Log.d(TAG, "initData: ");
            mCourseInfoBean = mCourseViewModel.findCourseById(getIntent().getIntExtra("CourseId", 0));
            isEditMode = mCourseInfoBean != null;
            if (null == mCourseInfoBean) {
                mCourseInfoBean = new CourseInfoBean();
            }
        }
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.edit_tool_bar);
        toolbar.setTitle(isEditMode ? R.string.edit_course_info : R.string.add_new_course);
        toolbar.setPadding(0, DisplayUtil.getStatusBarHeight(mContext), 0, 0);
        toolbar.setNavigationOnClickListener(v -> {
            if (TextUtils.isEmpty(mCourseInfoBean.getName())) {
                finish();
            } else {
                showSaveDialog();
            }
        });
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.delete) {
                if (TextUtils.isEmpty(mCourseInfoBean.getName())) {
                    finish();
                } else {
                    showDeleteTipDialog();
                }
            }
            return true;
        });

        RadioGroup weekTypeRgb = findViewById(R.id.add_course_rgb_week_type);
        weekTypeRgb.setVisibility(mCourseInfoBean.getWeekType() != 0 ? View.VISIBLE : View.GONE);
        weekTypeRgb.check(mCourseInfoBean.getWeekType() == 1 ? R.id.add_course_radio_odd : R.id.add_course_radio_even);
        weekTypeRgb.setOnCheckedChangeListener(this);
        mCourseNameEdt = findViewById(R.id.add_course_name);
        mCourseNameEdt.setText(TextUtils.isEmpty(mCourseInfoBean.getName()) ? "" : mCourseInfoBean.getName());

        mStartWeekIndexEdt = findViewById(R.id.edit_edt_start_week);
        mStartWeekIndexEdt.setText(mCourseInfoBean.getBeginWeek() == 0 ? "1" : String.valueOf(mCourseInfoBean.getBeginWeek()));

        mEndWeekIndexEdt = findViewById(R.id.edit_edt_end_week);
        mEndWeekIndexEdt.setText(mCourseInfoBean.getEndWeek() == 0 ? "18" : String.valueOf(mCourseInfoBean.getEndWeek()));

        mClassroomEdt = findViewById(R.id.add_course_classroom);
        mClassroomEdt.setText(TextUtils.isEmpty(mCourseInfoBean.getClassroom()) ? "" : mCourseInfoBean.getClassroom());

        mTeacherNameEdt = findViewById(R.id.add_course_teacher);
        mTeacherNameEdt.setText(TextUtils.isEmpty(mCourseInfoBean.getTeacher()) ? "" : mCourseInfoBean.getTeacher());

        Spinner weekDaySpinner = findViewById(R.id.add_course_spinner_week);
        weekDaySpinner.setSelection(TextUtils.isEmpty(mCourseInfoBean.getWeekTime()) ? 0 : Integer.parseInt(mCourseInfoBean.getWeekTime()) - 1);
        weekDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCourseInfoBean.setWeekTime(String.valueOf(position + 1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCourseInfoBean.setWeekTime("1");
            }
        });

        mStartClassIndex = findViewById(R.id.edit_edt_start_index);
        mStartClassIndex.setText(mCourseInfoBean.getBeginTime() == 0 ? "1" : String.valueOf(mCourseInfoBean.getBeginTime()));

        mCourseLengthEdt = findViewById(R.id.edit_edt_class_length);
        mCourseLengthEdt.setText(mCourseInfoBean.getLength() == 0 ? "1" : String.valueOf(mCourseInfoBean.getLength()));

        CheckBox isOddWeekCheck = findViewById(R.id.edit_ck_is_odd_or_even);
        isOddWeekCheck.setChecked(mCourseInfoBean.getWeekType() != 0);
        isOddWeekCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            weekTypeRgb.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        Button saveToDb = findViewById(R.id.add_course_btn_save);
        saveToDb.setOnClickListener(v -> saveCourseInfo());
    }

    private void showSaveDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext)
                .setMessage("是否保存课程信息")
                .setPositiveButton("保存", (dialog1, which) -> {
                    mCourseViewModel.saveCourseInfo(mCourseInfoBean);
                    finish();
                })
                .setNegativeButton("取消", (dialog12, which) -> {
                    dialog12.dismiss();
                    finish();
                });
        dialog.show();
    }

    private void showDeleteTipDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext)
                .setMessage("删除后，该课程将从课程表中移除")
                .setPositiveButton("删除", (dialog1, which) -> {
                    mCourseViewModel.deleteCourseInfo(mCourseInfoBean);
                    finish();
                })
                .setNegativeButton("取消", (dialog12, which) -> dialog12.dismiss());
        dialog.show();
    }

    private void saveCourseInfo() {
        if (TextUtils.isEmpty(mCourseNameEdt.getText().toString())) {
            Toast.makeText(mContext, "请输入课程名称", Toast.LENGTH_SHORT).show();
            return;
        }
        mCourseInfoBean.setName(mCourseNameEdt.getText().toString().trim());
        mCourseInfoBean.setBeginWeek(Integer.parseInt(mStartWeekIndexEdt.getText().toString().trim()));
        mCourseInfoBean.setEndWeek(Integer.parseInt(mEndWeekIndexEdt.getText().toString().trim()));
        mCourseInfoBean.setBeginTime(Integer.parseInt(mStartClassIndex.getText().toString().trim()));
        mCourseInfoBean.setClassroom(mClassroomEdt.getText().toString().trim());
        mCourseInfoBean.setTeacher(mTeacherNameEdt.getText().toString().trim());
        mCourseInfoBean.setLength(Integer.parseInt(mCourseLengthEdt.getText().toString().trim()));
        if (isEditMode) {
            mCourseViewModel.updateCourseInfo(mCourseInfoBean);
        } else {
            mCourseViewModel.saveCourseInfo(mCourseInfoBean);
        }
        finish();
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
