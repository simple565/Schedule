package com.maureen.schedule.view.course

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.maureen.schedule.R
import com.maureen.schedule.database.CourseInfo
import com.maureen.schedule.databinding.FragmentEditCourseBinding
import com.maureen.schedule.view.BaseFragment


/**
 * Function:
 * Create:   2020/11/27
 *
 * @author lianml
 */
class EditCourseFragment : BaseFragment(), RadioGroup.OnCheckedChangeListener {
    private var isEditMode = false
    private lateinit var viewBinding: FragmentEditCourseBinding
    private var mCourseInfo: CourseInfo? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentEditCourseBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
    }

    private fun initData() {
        isEditMode = false
        arguments?.run {

        }
    }

    private fun initView() {
//        setupWithNavController(viewBinding.editCourseToolBar)
        mCourseInfo?.run {
            viewBinding.addCourseRgbWeekType.visibility =
                if (weekType != 0) View.VISIBLE else View.GONE
            viewBinding.addCourseRgbWeekType.check(if (weekType == 1) R.id.add_course_radio_odd else R.id.add_course_radio_even)
            viewBinding.addCourseRgbWeekType.setOnCheckedChangeListener(this@EditCourseFragment)
            viewBinding.addCourseName.setText(if (TextUtils.isEmpty(name)) "" else name)
            viewBinding.editEdtStartWeek.setText(if (beginWeek == 0) "1" else beginWeek.toString())
            viewBinding.editEdtEndWeek.setText(if (endWeek == 0) "18" else endWeek.toString())
            viewBinding.addCourseClassroom.setText(if (TextUtils.isEmpty(classroom)) "" else classroom)
            viewBinding.addCourseTeacher.setText(if (TextUtils.isEmpty(teacher)) "" else teacher)
            viewBinding.addCourseSpinnerWeek.setSelection(if (TextUtils.isEmpty(weekTime)) 0 else weekTime.toInt() - 1)
            viewBinding.addCourseSpinnerWeek.onItemSelectedListener =
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
            viewBinding.editEdtStartIndex.setText(if (beginTime == 0) "1" else beginTime.toString())
            viewBinding.editEdtClassLength.setText(if (length == 0) "1" else length.toString())
            viewBinding.editCkIsOddOrEven.isChecked = weekType != 0
            viewBinding.editCkIsOddOrEven.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                Log.d(TAG, "initView: is odd or even $isChecked")
                viewBinding.addCourseRgbWeekType.visibility =
                    if (isChecked) View.VISIBLE else View.GONE
            }
            viewBinding.addCourseBtnSave.setOnClickListener { saveCourseInfo() }
        }
    }

    private fun showSaveDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("是否保存课程信息")
            .setPositiveButton("保存") { _: DialogInterface?, _: Int ->

                findNavController().navigateUp()
            }
            .setNegativeButton("取消") { dialog12: DialogInterface, _: Int ->
                dialog12.dismiss()
                findNavController().navigateUp()
            }
            .show()
    }

    private fun showDeleteTipDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("删除后，该课程将从课程表中移除")
            .setPositiveButton("删除") { _: DialogInterface?, _: Int ->
                Log.d(TAG, "showDeleteTipDialog: delete course")

                findNavController().navigateUp()
            }
            .setNegativeButton("取消") { dialog12: DialogInterface, _: Int -> dialog12.dismiss() }
            .show()
    }

    private fun saveCourseInfo() {
        if (TextUtils.isEmpty(viewBinding.addCourseName.text.toString())) {
            Toast.makeText(requireContext(), "请输入课程名称", Toast.LENGTH_SHORT).show()
            return
        }
        mCourseInfo?.apply {
            name = viewBinding.addCourseName.text.toString()
            beginWeek = viewBinding.editEdtStartWeek.text.toString().trim { it <= ' ' }.toInt()
            endWeek = viewBinding.editEdtEndWeek.text.toString().trim { it <= ' ' }.toInt()
            beginTime = viewBinding.editEdtStartIndex.text.toString().trim { it <= ' ' }.toInt()
            classroom = viewBinding.addCourseClassroom.text.toString()
            teacher = viewBinding.addCourseTeacher.text.toString()
            length = viewBinding.editEdtClassLength.text.toString().trim { it <= ' ' }.toInt()
            if (isEditMode) {
                Log.d(TAG, "saveCourseInfo: update course info")
                //courseViewModel.updateCourseInfo(mCourseInfo!!)
            } else {
                Log.d(TAG, "saveCourseInfo: save course info")
                //courseViewModel.saveCourseInfo(mCourseInfo!!)
            }
        }
        findNavController().navigateUp()
    }

    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        when (checkedId) {
            R.id.add_course_radio_odd -> mCourseInfo!!.weekType = 1
            R.id.add_course_radio_even -> mCourseInfo!!.weekType = 2
        }
    }

    companion object {
        private const val TAG = "EditCourseFragment"
    }
}