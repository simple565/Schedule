package com.maureen.schedule.view.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.maureen.schedule.CourseViewModel
import com.maureen.schedule.R
import com.maureen.schedule.data.CourseInfoBean
import com.maureen.schedule.databinding.ActivityMainBinding
import com.maureen.schedule.utils.DateUtil
import com.maureen.schedule.utils.DisplayUtil
import com.maureen.schedule.utils.TOTAL_COURSE_COUNT
import com.maureen.schedule.utils.WEEK_LENGTH
import java.util.*

/**
 * @author lianml
 */
class MainActivity : AppCompatActivity() {
    private lateinit var mContext: Context
    private var mCourseItemViews: HashMap<Int, TextView>? = null
    private var mWeekIndex: String? = null
    private var mMonthIndex: String? = null
    private lateinit var mCourseViewModel: CourseViewModel
    private lateinit var mViewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
        mViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
        mContext = this
        initData()
        initView()
    }

    private fun initView() {
        with(mViewBinding.mainToolBar) {
            setPadding(0, DisplayUtil.getStatusBarHeight(mContext), 0, 0)
            title = mWeekIndex
        }
        mViewBinding.mainTvMonth.text = mMonthIndex
        // 初始化日期栏
        val weekDayViewWidth = (DisplayUtil.getScreenWidth(mContext) - DisplayUtil.dp2px(mContext, 32f)) / 7
        val weekDays = DateUtil.getDayOfCurrentWeek(WEEK_LENGTH)
        for (i in 0 until WEEK_LENGTH) {
            val day = weekDays[i]
            val view = TextView(mContext)
            view.width = weekDayViewWidth
            view.gravity = Gravity.CENTER
            view.text = String.format("%s\n%s", day.weekIndex, day.date)
            if (day.isCurrent) {
                view.setBackgroundResource(R.drawable.course_bg_blue)
                view.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            }
            mViewBinding.mainLlWeek.addView(view)
        }

        // 初始化节数栏
        val countLayout = findViewById<LinearLayout>(R.id.main_ll_course_count)
        for (i in 0 until TOTAL_COURSE_COUNT) {
            val view = TextView(mContext)
            view.height = DisplayUtil.dp2px(mContext, 64f)
            view.gravity = Gravity.CENTER
            view.text = (i + 1).toString()
            countLayout.addView(view)
        }
        with(mViewBinding.mainGlTable) {
            columnCount = WEEK_LENGTH
            rowCount = TOTAL_COURSE_COUNT
        }
        initTableView()
    }

    private fun initData() {
        mCourseViewModel = ViewModelProvider(this).get(CourseViewModel::class.java)
        mWeekIndex = "第一周"
        mMonthIndex = DateUtil.currentMonth
        mCourseViewModel.getAllCourseInfo().observe(this, { courseInfoBeans: List<CourseInfoBean> ->
            if (mCourseItemViews?.size != 0) {
                Log.d(TAG, "initData: view map size: " + mCourseItemViews?.size)
                mCourseItemViews?.forEach { (_, view) ->
                    run {
                        mViewBinding.mainGlTable.removeView(view)
                    }
                }
            }
            courseInfoBeans.forEach { x ->
                run {
                    mViewBinding.mainGlTable.addView(mCourseItemViews?.get(x.id) ?: run {
                        genCourseItemView(x)
                    })
                }
            }
        })
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
                        height = DisplayUtil.dp2px(mContext, 64f)
                    }
                    setGravity(Gravity.FILL)
                }
                val courseInfoView = TextView(mContext).apply {
                    setBackgroundResource(R.color.white)
                    this.layoutParams = layoutParams
                    setOnClickListener { jumpToEditCourseInfo(null) }
                }
                mViewBinding.mainGlTable.addView(courseInfoView)
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
        return TextView(mContext).apply {
            gravity = Gravity.CENTER
            text = String.format("%1s@%2s", courseInfo.name, courseInfo.classroom)
            this.layoutParams = layoutParams
            setTextColor(ContextCompat.getColor(mContext, R.color.white))
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
        val intent = Intent(this@MainActivity, EditCourseActivity::class.java)
        courseInfo?.run {
            Log.d(TAG, "jumpToEditCourseInfo: $id")
            intent.putExtra("CourseId", id)
        }
        startActivity(intent)
    }

    private fun showDeleteTipDialog(view: View, courseInfoBean: CourseInfoBean) {
        AlertDialog.Builder(mContext)
                .setMessage("删除后，该课程将从课程表中移除")
                .setPositiveButton("删除") { _: DialogInterface?, _: Int ->
                    mCourseViewModel.deleteCourseInfo(courseInfoBean)
                    mViewBinding.mainGlTable.removeView(view)
                }
                .setNegativeButton("取消") { dialog12: DialogInterface, _: Int -> dialog12.dismiss() }
                .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCourseViewModel.getAllCourseInfo().removeObservers(this)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}