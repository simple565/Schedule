package com.maureen.schedule.view.activity

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.maureen.schedule.CourseViewModel
import com.maureen.schedule.R
import com.maureen.schedule.data.CourseInfoBean
import com.maureen.schedule.databinding.ActivityEditCourseBinding
import com.maureen.schedule.utils.DisplayUtil
import com.maureen.schedule.utils.KEY_COURSE_INFO_ID


/**
 * Function:
 * Create:   2020/11/27
 *
 * @author lianml
 */
class EditCourseActivity : AppCompatActivity(), RadioGroup.OnCheckedChangeListener {
    private lateinit var mCourseViewModel: CourseViewModel
    private var isEditMode = false
    private lateinit var mContext: Context
    private lateinit var mViewBinding: ActivityEditCourseBinding
    private var mCourseInfoBean: CourseInfoBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
        mViewBinding = ActivityEditCourseBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
        mContext = this
        initData()
        initView()
    }

    private fun initData() {
        isEditMode = false
        mCourseViewModel = ViewModelProvider(this).get(CourseViewModel::class.java)
        intent?.run {
            mCourseInfoBean =
                mCourseViewModel.findCourseById(intent.getIntExtra(KEY_COURSE_INFO_ID, -1))
                    ?: run {
                        CourseInfoBean()
                    }
        }
    }

    private fun initView() {
        with(mViewBinding.editToolBar) {
            setTitle(if (isEditMode) R.string.edit_course_info else R.string.add_new_course)
            setPadding(0, DisplayUtil.getStatusBarHeight(mContext), 0, 0)
            setNavigationOnClickListener {
                if (TextUtils.isEmpty(mCourseInfoBean!!.name)) finish() else showSaveDialog()
            }
            setOnMenuItemClickListener { item: MenuItem ->
                if (item.itemId == R.id.delete) {
                    if (TextUtils.isEmpty(mCourseInfoBean!!.name)) finish() else showDeleteTipDialog()
                }
                true
            }
        }
        mCourseInfoBean?.run {
            mViewBinding.addCourseRgbWeekType.visibility = if (weekType != 0) View.VISIBLE else View.GONE
            mViewBinding.addCourseRgbWeekType.check(if (weekType == 1) R.id.add_course_radio_odd else R.id.add_course_radio_even)
            mViewBinding.addCourseRgbWeekType.setOnCheckedChangeListener(this@EditCourseActivity)
            mViewBinding.addCourseName.setText(if (TextUtils.isEmpty(name)) "" else name)
            mViewBinding.editEdtStartWeek.setText(if (beginWeek == 0) "1" else beginWeek.toString())
            mViewBinding.editEdtEndWeek.setText(if (endWeek == 0) "18" else endWeek.toString())
            mViewBinding.addCourseClassroom.setText(if (TextUtils.isEmpty(classroom)) "" else classroom)
            mViewBinding.addCourseTeacher.setText(if (TextUtils.isEmpty(teacher)) "" else teacher)
            mViewBinding.addCourseSpinnerWeek.setSelection(if (TextUtils.isEmpty(weekTime)) 0 else weekTime.toInt() - 1)
            mViewBinding.addCourseSpinnerWeek.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                    weekTime = (position + 1).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    weekTime = "1"
                }
            }
            mViewBinding.editEdtStartIndex.setText(if (beginTime == 0) "1" else beginTime.toString())
            mViewBinding.editEdtClassLength.setText(if (length == 0) "1" else length.toString())
            mViewBinding.editCkIsOddOrEven.isChecked = weekType != 0
            mViewBinding.editCkIsOddOrEven.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean -> mViewBinding.addCourseRgbWeekType.visibility = if (isChecked) View.VISIBLE else View.GONE }
            mViewBinding.addCourseBtnSave.setOnClickListener { saveCourseInfo() }
        }
    }

    private fun showSaveDialog() {
        AlertDialog.Builder(mContext)
                .setMessage("是否保存课程信息")
                .setPositiveButton("保存") { _: DialogInterface?, _: Int ->
                    mCourseViewModel.saveCourseInfo(mCourseInfoBean!!)
                    finish()
                }
                .setNegativeButton("取消") { dialog12: DialogInterface, _: Int ->
                    dialog12.dismiss()
                    finish()
                }
                .show()
    }

    private fun showDeleteTipDialog() {
        AlertDialog.Builder(mContext)
                .setMessage("删除后，该课程将从课程表中移除")
                .setPositiveButton("删除") { _: DialogInterface?, _: Int ->
                    Log.d(TAG, "showDeleteTipDialog: delete course")
                    mCourseViewModel.deleteCourseInfo(mCourseInfoBean!!)
                    finish()
                }
                .setNegativeButton("取消") { dialog12: DialogInterface, _: Int -> dialog12.dismiss() }
                .show()
    }

    private fun saveCourseInfo() {
        if (TextUtils.isEmpty(mViewBinding.addCourseName.text.toString())) {
            Toast.makeText(mContext, "请输入课程名称", Toast.LENGTH_SHORT).show()
            return
        }
        mCourseInfoBean?.apply {
            name = mViewBinding.addCourseName.text.toString()
            beginWeek = mViewBinding.editEdtStartWeek.text.toString().trim { it <= ' ' }.toInt()
            endWeek = mViewBinding.editEdtEndWeek.text.toString().trim { it <= ' ' }.toInt()
            beginTime = mViewBinding.editEdtStartIndex.text.toString().trim { it <= ' ' }.toInt()
            classroom = mViewBinding.addCourseClassroom.text.toString()
            teacher = mViewBinding.addCourseTeacher.text.toString()
            length = mViewBinding.editEdtClassLength.text.toString().trim { it <= ' ' }.toInt()
            if (isEditMode) {
                Log.d(TAG, "saveCourseInfo: update course info")
                mCourseViewModel.updateCourseInfo(mCourseInfoBean!!)
            } else {
                Log.d(TAG, "saveCourseInfo: save course info")
                mCourseViewModel.saveCourseInfo(mCourseInfoBean!!)
            }
        }
        finish()
    }

    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        when (checkedId) {
            R.id.add_course_radio_odd -> mCourseInfoBean!!.weekType = 1
            R.id.add_course_radio_even -> mCourseInfoBean!!.weekType = 2
        }
    }

    companion object {
        private const val TAG = "EditCourseActivity"
    }
}