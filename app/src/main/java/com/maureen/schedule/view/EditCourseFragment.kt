package com.maureen.schedule.view

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.maureen.schedule.CourseViewModel
import com.maureen.schedule.R
import com.maureen.schedule.data.CourseInfoBean
import com.maureen.schedule.databinding.FragmentEditCourseBinding
import com.maureen.schedule.utils.DisplayUtil
import com.maureen.schedule.utils.KEY_COURSE_INFO_ID


/**
 * Function:
 * Create:   2020/11/27
 *
 * @author lianml
 */
class EditCourseFragment : Fragment(), RadioGroup.OnCheckedChangeListener {
    private val courseViewModel: CourseViewModel by viewModels()
    private var isEditMode = false
    private val mViewBinding by lazy {
        FragmentEditCourseBinding.inflate(layoutInflater)
    }
    private var mCourseInfoBean: CourseInfoBean? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
    }

    private fun initData() {
        isEditMode = false
        arguments?.run {
            courseViewModel.findCourseById(getInt(KEY_COURSE_INFO_ID, -1))
        }
        courseViewModel.courseLiveData.observe(viewLifecycleOwner, {

        })
    }

    private fun initView() {
        with(mViewBinding.editToolBar) {
            setTitle(if (isEditMode) R.string.edit_course_info else R.string.add_new_course)
            setPadding(0, DisplayUtil.getStatusBarHeight(requireContext()), 0, 0)
            setNavigationOnClickListener {
                //if (TextUtils.isEmpty(mCourseInfoBean!!.name)) finish() else showSaveDialog()
            }
            setOnMenuItemClickListener { item: MenuItem ->
                if (item.itemId == R.id.delete) {
                    //if (TextUtils.isEmpty(mCourseInfoBean!!.name)) finish() else showDeleteTipDialog()
                }
                true
            }
        }
        mCourseInfoBean?.run {
            mViewBinding.addCourseRgbWeekType.visibility =
                if (weekType != 0) View.VISIBLE else View.GONE
            mViewBinding.addCourseRgbWeekType.check(if (weekType == 1) R.id.add_course_radio_odd else R.id.add_course_radio_even)
            mViewBinding.addCourseRgbWeekType.setOnCheckedChangeListener(this@EditCourseFragment)
            mViewBinding.addCourseName.setText(if (TextUtils.isEmpty(name)) "" else name)
            mViewBinding.editEdtStartWeek.setText(if (beginWeek == 0) "1" else beginWeek.toString())
            mViewBinding.editEdtEndWeek.setText(if (endWeek == 0) "18" else endWeek.toString())
            mViewBinding.addCourseClassroom.setText(if (TextUtils.isEmpty(classroom)) "" else classroom)
            mViewBinding.addCourseTeacher.setText(if (TextUtils.isEmpty(teacher)) "" else teacher)
            mViewBinding.addCourseSpinnerWeek.setSelection(if (TextUtils.isEmpty(weekTime)) 0 else weekTime.toInt() - 1)
            mViewBinding.addCourseSpinnerWeek.onItemSelectedListener =
                object : OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        weekTime = (position + 1).toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        weekTime = "1"
                    }
                }
            mViewBinding.editEdtStartIndex.setText(if (beginTime == 0) "1" else beginTime.toString())
            mViewBinding.editEdtClassLength.setText(if (length == 0) "1" else length.toString())
            mViewBinding.editCkIsOddOrEven.isChecked = weekType != 0
            mViewBinding.editCkIsOddOrEven.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                mViewBinding.addCourseRgbWeekType.visibility =
                    if (isChecked) View.VISIBLE else View.GONE
            }
            mViewBinding.addCourseBtnSave.setOnClickListener { saveCourseInfo() }
        }
    }

    private fun showSaveDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("是否保存课程信息")
            .setPositiveButton("保存") { _: DialogInterface?, _: Int ->
                courseViewModel.saveCourseInfo(mCourseInfoBean!!)
                //finish()
            }
            .setNegativeButton("取消") { dialog12: DialogInterface, _: Int ->
                dialog12.dismiss()
                //finish()
            }
            .show()
    }

    private fun showDeleteTipDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("删除后，该课程将从课程表中移除")
            .setPositiveButton("删除") { _: DialogInterface?, _: Int ->
                Log.d(TAG, "showDeleteTipDialog: delete course")
                courseViewModel.deleteCourseInfo(mCourseInfoBean!!)
                //finish()
            }
            .setNegativeButton("取消") { dialog12: DialogInterface, _: Int -> dialog12.dismiss() }
            .show()
    }

    private fun saveCourseInfo() {
        if (TextUtils.isEmpty(mViewBinding.addCourseName.text.toString())) {
            Toast.makeText(requireContext(), "请输入课程名称", Toast.LENGTH_SHORT).show()
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
                courseViewModel.updateCourseInfo(mCourseInfoBean!!)
            } else {
                Log.d(TAG, "saveCourseInfo: save course info")
                courseViewModel.saveCourseInfo(mCourseInfoBean!!)
            }
        }
        //finish()
    }

    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        when (checkedId) {
            R.id.add_course_radio_odd -> mCourseInfoBean!!.weekType = 1
            R.id.add_course_radio_even -> mCourseInfoBean!!.weekType = 2
        }
    }

    companion object {
        private const val TAG = "EditCourseFragment"
    }
}