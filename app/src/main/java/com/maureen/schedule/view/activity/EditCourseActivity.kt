package com.maureen.schedule.view.activity

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.maureen.schedule.CourseViewModel
import com.maureen.schedule.R
import com.maureen.schedule.data.CourseInfoBean
import com.maureen.schedule.utils.DisplayUtil


/**
 * Function:
 * Create:   2020/11/27
 *
 * @author lianml
 */
class EditCourseActivity : AppCompatActivity(), RadioGroup.OnCheckedChangeListener {
    private var mCourseViewModel: CourseViewModel? = null
    private var mCourseInfoBean: CourseInfoBean? = null
    private var mCourseNameEdt: EditText? = null
    private var mClassroomEdt: EditText? = null
    private var mTeacherNameEdt: EditText? = null
    private var mCourseLengthEdt: EditText? = null
    private var mStartWeekIndexEdt: EditText? = null
    private var mEndWeekIndexEdt: EditText? = null
    private var mStartClassIndex: EditText? = null
    private var isEditMode = false
    private var mContext: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.fragment_edit_course)
        mContext = this
        initData()
        initView()
    }

    private fun initData() {
        isEditMode = false
        mCourseViewModel = ViewModelProvider(this).get(CourseViewModel::class.java)

        intent?.run {
            mCourseViewModel!!.findCourseById(intent.getIntExtra("CourseId", -1))?.apply {
                mCourseInfoBean = this
            } ?: run {
                mCourseInfoBean = CourseInfoBean()
            }
        }
    }

    private fun initView() {
        val toolbar = findViewById<Toolbar>(R.id.edit_tool_bar)
        toolbar.setTitle(if (isEditMode) R.string.edit_course_info else R.string.add_new_course)
        toolbar.setPadding(0, DisplayUtil.getStatusBarHeight(mContext!!), 0, 0)
        toolbar.setNavigationOnClickListener {
            if (TextUtils.isEmpty(mCourseInfoBean!!.name)) {
                finish()
            } else {
                showSaveDialog()
            }
        }
        toolbar.setOnMenuItemClickListener { item: MenuItem ->
            if (item.itemId == R.id.delete) {
                if (TextUtils.isEmpty(mCourseInfoBean!!.name)) {
                    finish()
                } else {
                    showDeleteTipDialog()
                }
            }
            true
        }
        val weekTypeRgb = findViewById<RadioGroup>(R.id.add_course_rgb_week_type)
        weekTypeRgb.visibility = if (mCourseInfoBean!!.weekType != 0) View.VISIBLE else View.GONE
        weekTypeRgb.check(if (mCourseInfoBean!!.weekType == 1) R.id.add_course_radio_odd else R.id.add_course_radio_even)
        weekTypeRgb.setOnCheckedChangeListener(this)
        mCourseNameEdt = findViewById(R.id.add_course_name)
        mCourseNameEdt?.setText(if (TextUtils.isEmpty(mCourseInfoBean!!.name)) "" else mCourseInfoBean!!.name)
        mStartWeekIndexEdt = findViewById(R.id.edit_edt_start_week)
        mStartWeekIndexEdt?.setText(if (mCourseInfoBean!!.beginWeek == 0) "1" else mCourseInfoBean!!.beginWeek.toString())
        mEndWeekIndexEdt = findViewById(R.id.edit_edt_end_week)
        mEndWeekIndexEdt?.setText(if (mCourseInfoBean!!.endWeek == 0) "18" else mCourseInfoBean!!.endWeek.toString())
        mClassroomEdt = findViewById(R.id.add_course_classroom)
        mClassroomEdt?.setText(if (TextUtils.isEmpty(mCourseInfoBean!!.classroom)) "" else mCourseInfoBean!!.classroom)
        mTeacherNameEdt = findViewById(R.id.add_course_teacher)
        mTeacherNameEdt?.setText(if (TextUtils.isEmpty(mCourseInfoBean!!.teacher)) "" else mCourseInfoBean!!.teacher)
        val weekDaySpinner = findViewById<Spinner>(R.id.add_course_spinner_week)
        weekDaySpinner.setSelection(if (TextUtils.isEmpty(mCourseInfoBean!!.weekTime)) 0 else mCourseInfoBean!!.weekTime.toInt() - 1)
        weekDaySpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                mCourseInfoBean!!.weekTime = (position + 1).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                mCourseInfoBean!!.weekTime = "1"
            }
        }
        mStartClassIndex = findViewById(R.id.edit_edt_start_index)
        mStartClassIndex?.setText(if (mCourseInfoBean!!.beginTime == 0) "1" else mCourseInfoBean!!.beginTime.toString())
        mCourseLengthEdt = findViewById(R.id.edit_edt_class_length)
        mCourseLengthEdt?.setText(if (mCourseInfoBean!!.length == 0) "1" else mCourseInfoBean!!.length.toString())
        val isOddWeekCheck = findViewById<CheckBox>(R.id.edit_ck_is_odd_or_even)
        isOddWeekCheck.isChecked = mCourseInfoBean!!.weekType != 0
        isOddWeekCheck.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean -> weekTypeRgb.visibility = if (isChecked) View.VISIBLE else View.GONE }
        val saveToDb = findViewById<Button>(R.id.add_course_btn_save)
        saveToDb.setOnClickListener { saveCourseInfo() }
    }

    private fun showSaveDialog() {
        val dialog = AlertDialog.Builder(mContext!!)
                .setMessage("是否保存课程信息")
                .setPositiveButton("保存") { _: DialogInterface?, _: Int ->
                    mCourseViewModel!!.saveCourseInfo(mCourseInfoBean!!)
                    finish()
                }
                .setNegativeButton("取消") { dialog12: DialogInterface, _: Int ->
                    dialog12.dismiss()
                    finish()
                }
        dialog.show()
    }

    private fun showDeleteTipDialog() {
        val dialog = AlertDialog.Builder(mContext!!)
                .setMessage("删除后，该课程将从课程表中移除")
                .setPositiveButton("删除") { _: DialogInterface?, _: Int ->
                    mCourseViewModel!!.deleteCourseInfo(mCourseInfoBean!!)
                    finish()
                }
                .setNegativeButton("取消") { dialog12: DialogInterface, _: Int -> dialog12.dismiss() }
        dialog.show()
    }

    private fun saveCourseInfo() {
        if (TextUtils.isEmpty(mCourseNameEdt!!.text.toString())) {
            Toast.makeText(mContext, "请输入课程名称", Toast.LENGTH_SHORT).show()
            return
        }
        mCourseInfoBean!!.name = mCourseNameEdt!!.text.toString().trim { it <= ' ' }
        mCourseInfoBean!!.beginWeek = mStartWeekIndexEdt!!.text.toString().trim { it <= ' ' }.toInt()
        mCourseInfoBean!!.endWeek = mEndWeekIndexEdt!!.text.toString().trim { it <= ' ' }.toInt()
        mCourseInfoBean!!.beginTime = mStartClassIndex!!.text.toString().trim { it <= ' ' }.toInt()
        mCourseInfoBean!!.classroom = mClassroomEdt!!.text.toString().trim { it <= ' ' }
        mCourseInfoBean!!.teacher = mTeacherNameEdt!!.text.toString().trim { it <= ' ' }
        mCourseInfoBean!!.length = mCourseLengthEdt!!.text.toString().trim { it <= ' ' }.toInt()
        if (isEditMode) {
            mCourseViewModel!!.updateCourseInfo(mCourseInfoBean!!)
        } else {
            mCourseViewModel!!.saveCourseInfo(mCourseInfoBean!!)
        }
        finish()
    }

    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        if (checkedId == R.id.add_course_radio_odd) {
            //单周
            mCourseInfoBean!!.weekType = 1
        } else if (checkedId == R.id.add_course_radio_even) {
            //双周
            mCourseInfoBean!!.weekType = 2
        }
    }

    companion object {
        private const val TAG = "EditCourseActivity"
    }
}