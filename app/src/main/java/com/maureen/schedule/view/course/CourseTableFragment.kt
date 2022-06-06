package com.maureen.schedule.view.course

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.maureen.schedule.R
import com.maureen.schedule.database.CourseInfo
import com.maureen.schedule.databinding.FragmentCourseTableBinding
import com.maureen.schedule.utils.*
import com.maureen.schedule.view.BaseFragment
import java.util.*

/**
 * @author lianml
 */
class CourseTableFragment : BaseFragment() {
    private var mCourseItemViews: HashMap<Long, TextView>? = null
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
//        setupWithNavController(viewBinding.courseToolBar)
        viewBinding.ctTvMonth.text = mMonthIndex
        // 初始化日期栏
        val weekDayViewWidth =
            (DisplayUtil.getScreenWidth(requireContext()) - DisplayUtil.dp2px(
                requireContext(),
                32f
            )) / 7
        val weekDays = getDayOfCurrentWeek(WEEK_LENGTH)
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
        mMonthIndex = System.currentTimeMillis().convertToDate("MM月")
        /*courseViewModel.courseListLiveData.observe(
            viewLifecycleOwner,
            { courseInfoBeans: List<CourseInfo> ->
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
                    height = 0
                    width = 0
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

    private fun genCourseItemView(courseInfo: CourseInfo): TextView {
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

    private fun jumpToEditCourseInfo(courseInfo: CourseInfo?) {
        val bundle = if (null == courseInfo) {
            bundleOf(KEY_TASK_ID to -1)
        } else {
            bundleOf(KEY_TASK_ID to courseInfo.id)
        }
    }

    private fun showDeleteTipDialog(view: View, courseInfo: CourseInfo) {
        AlertDialog.Builder(requireContext())
            .setMessage("删除后，该课程将从课程表中移除")
            .setPositiveButton("删除") { _: DialogInterface?, _: Int ->
                //courseViewModel.deleteCourseInfo(courseInfo)
                viewBinding.ctGlTable.removeView(view)
            }
            .setNegativeButton("取消") { dialog12: DialogInterface, _: Int -> dialog12.dismiss() }
            .show()
    }
}