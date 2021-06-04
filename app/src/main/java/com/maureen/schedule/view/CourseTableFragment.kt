package com.maureen.schedule.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.maureen.schedule.R
import com.maureen.schedule.data.CourseInfoBean
import com.maureen.schedule.databinding.FragmentCourseTableBinding
import com.maureen.schedule.utils.*
import java.util.*

/**
 * @author lianml
 */
class CourseTableFragment : Fragment() {
    private var mCourseItemViews: HashMap<Int, TextView>? = null
    private var mWeekIndex: String? = null
    private var mMonthIndex: String? = null

    private lateinit var viewBinding: FragmentCourseTableBinding

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentCourseTableBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
    }

    private fun initView() {
        viewBinding.ctTvMonth.text = mMonthIndex
        // 初始化日期栏
        val weekDayViewWidth =
            (DisplayUtil.getScreenWidth(requireContext()) - DisplayUtil.dp2px(
                requireContext(),
                32f
            )) / 7
        val weekDays = DateUtil.getDayOfCurrentWeek(WEEK_LENGTH)
        for (i in 0 until WEEK_LENGTH) {
            val day = weekDays[i]
            val view = TextView(requireContext())
            view.width = weekDayViewWidth
            view.gravity = Gravity.CENTER
            view.text = String.format("%s\n%s", day.weekIndex, day.date)
            if (day.isCurrent) {
                view.setBackgroundResource(R.drawable.course_bg_blue)
                view.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
            viewBinding.ctLlWeek.addView(view)
        }

        // 初始化节数栏
        for (i in 0 until TOTAL_COURSE_COUNT) {
            val view = TextView(requireContext())
            view.height = DisplayUtil.dp2px(requireContext(), 64f)
            view.gravity = Gravity.CENTER
            view.text = (i + 1).toString()
            viewBinding.ctLlCourseCount.addView(view)
        }
        with(viewBinding.ctGlTable) {
            columnCount = WEEK_LENGTH
            rowCount = TOTAL_COURSE_COUNT
        }
        initTableView()
    }

    private fun initData() {
        mWeekIndex = "第一周"
        mMonthIndex = DateUtil.currentMonth
        /*courseViewModel.courseListLiveData.observe(
            viewLifecycleOwner,
            { courseInfoBeans: List<CourseInfoBean> ->
                if (mCourseItemViews?.size != 0) {
                    Log.d(TAG, "initData: view map size: " + mCourseItemViews?.size)
                    mCourseItemViews?.forEach { (_, view) ->
                        run {
                            viewBinding.ctGlTable.removeView(view)
                        }
                    }
                }
                courseInfoBeans.forEach { x ->
                    run {
                        viewBinding.ctGlTable.addView(mCourseItemViews?.get(x.id) ?: run {
                            genCourseItemView(x)
                        })
                }
            }
        })*/
    }

    private fun initTableView() {
        for (i in 0 until WEEK_LENGTH) {
            for (j in 0 until TOTAL_COURSE_COUNT) {
                val columnSpec = GridLayout.spec(i, 1f)
                val rowSpec = GridLayout.spec(j, 1f)
                val layoutParams = GridLayout.LayoutParams(rowSpec, columnSpec).apply {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        height = 0
                        width = 0
                    } else {
                        width = 0
                        height = DisplayUtil.dp2px(requireContext(), 64f)
                    }
                    setGravity(Gravity.FILL)
                }
                val courseInfoView = TextView(requireContext()).apply {
                    setBackgroundResource(R.color.white)
                    this.layoutParams = layoutParams
                    setOnClickListener { jumpToEditCourseInfo(null) }
                }
                viewBinding.ctGlTable.addView(courseInfoView)
            }
        }
    }

    private fun genCourseItemView(courseInfo: CourseInfoBean): TextView {
        val rowSpec = GridLayout.spec(courseInfo.beginTime - 1, courseInfo.length, 1f)
        val columnSpec = GridLayout.spec(courseInfo.weekTime.toInt() - 1, 1, 1f)
        val layoutParams = GridLayout.LayoutParams(rowSpec, columnSpec).apply {
            height = 0
            width = 0
            setGravity(Gravity.FILL)
        }
        return TextView(requireContext()).apply {
            gravity = Gravity.CENTER
            text = String.format("%1s@%2s", courseInfo.name, courseInfo.classroom)
            this.layoutParams = layoutParams
            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            setBackgroundResource(R.drawable.course_bg_cyan)
            setOnClickListener { jumpToEditCourseInfo(courseInfo) }
            setOnLongClickListener { v: View ->
                showDeleteTipDialog(v, courseInfo)
                true
            }
            mCourseItemViews?.set(courseInfo.id, this)
        }
    }

    private fun jumpToEditCourseInfo(courseInfo: CourseInfoBean?) {
        val intent = Intent(requireContext(), EditCourseFragment::class.java)
        courseInfo?.run {
            Log.d(TAG, "jumpToEditCourseInfo: $id")
            intent.putExtra(KEY_COURSE_INFO_ID, id)
        }
        startActivity(intent)
    }

    private fun showDeleteTipDialog(view: View, courseInfoBean: CourseInfoBean) {
        AlertDialog.Builder(requireContext())
            .setMessage("删除后，该课程将从课程表中移除")
            .setPositiveButton("删除") { _: DialogInterface?, _: Int ->
                //courseViewModel.deleteCourseInfo(courseInfoBean)
                viewBinding.ctGlTable.removeView(view)
            }
            .setNegativeButton("取消") { dialog12: DialogInterface, _: Int -> dialog12.dismiss() }
            .show()
    }
}