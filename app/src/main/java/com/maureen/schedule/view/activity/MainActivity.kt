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
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.maureen.schedule.CourseViewModel
import com.maureen.schedule.R
import com.maureen.schedule.data.CourseInfoBean
import com.maureen.schedule.utils.DateUtil
import com.maureen.schedule.utils.DisplayUtil
import java.util.*

/**
 * @author lianml
 */
class MainActivity : AppCompatActivity() {
    private var mContext: Context? = null
    private var mCourseItemViews: HashMap<Int, TextView>? = null
    private var mMonthTv: TextView? = null
    private var mWeekIndex: String? = null
    private var mMonthIndex: String? = null
    private var mCourseViewModel: CourseViewModel? = null
    private var mTableLayout: GridLayout? = null
    private var mWeekLength = 7
    private var mTotalCourCount = 10
    private var mDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_main)
        mContext = this
        initData()
        initView()
    }

    private fun initView() {
        val toolBar = findViewById<Toolbar>(R.id.main_tool_bar)
        toolBar.setPadding(0, DisplayUtil.getStatusBarHeight(mContext!!), 0, 0)
        toolBar.title = mWeekIndex
        mMonthTv = findViewById(R.id.main_tv_month)
        mMonthTv?.text = mMonthIndex
        // 初始化日期栏
        val weekDayViewWidth = (DisplayUtil.getScreenWidth(mContext!!) - DisplayUtil.dp2px(mContext!!, 32f)) / 7
        val dateLayout = findViewById<LinearLayout>(R.id.main_ll_week)
        val weekDays = DateUtil.getDayOfCurrentWeek(mWeekLength)
        for (i in 0 until mWeekLength) {
            val day = weekDays[i]
            val view = TextView(mContext)
            view.width = weekDayViewWidth
            view.gravity = Gravity.CENTER
            view.text = String.format("%s\n%s", day.weekIndex, day.date)
            if (day.isCurrent) {
                view.setBackgroundResource(R.drawable.course_bg_blue)
                view.setTextColor(ContextCompat.getColor(mContext!!, R.color.white))
            }
            dateLayout.addView(view)
        }

        // 初始化节数栏
        val countLayout = findViewById<LinearLayout>(R.id.main_ll_course_count)
        for (i in 0 until mTotalCourCount) {
            val view = TextView(mContext)
            view.height = DisplayUtil.dp2px(mContext!!, 64f)
            view.gravity = Gravity.CENTER
            view.text = (i + 1).toString()
            countLayout.addView(view)
        }
        mTableLayout = findViewById(R.id.main_gl_table)
        mTableLayout?.columnCount = mWeekLength
        mTableLayout?.rowCount = mTotalCourCount
        initTableView()
    }

    private fun initData() {
        mWeekLength = 7
        mTotalCourCount = 10
        mCourseViewModel = ViewModelProvider(this).get(CourseViewModel::class.java)
        mWeekIndex = "第一周"
        mMonthIndex = DateUtil.currentMonth
        mCourseViewModel!!.getAllCourseInfo().observe(this, { courseInfoBeans: List<CourseInfoBean> ->
            if (mCourseItemViews?.size != 0) {
                mCourseItemViews?.forEach { (_, view) ->
                    run {
                        mTableLayout?.removeView(view)
                    }
                }
            }
            courseInfoBeans.forEach { x ->
                run {
                    var childView: TextView? = mCourseItemViews?.get(x.id)
                    if (null == childView) {
                        childView = genCourseItemView(x)
                    }
                    mTableLayout?.addView(childView)
                }
            }
        })
    }

    private fun initTableView() {
        for (i in 0 until mWeekLength) {
            for (j in 0 until mTotalCourCount) {
                val courseInfoView = TextView(mContext)
                courseInfoView.setBackgroundResource(R.color.white)
                val columnSpec = GridLayout.spec(i, 1f)
                val rowSpec = GridLayout.spec(j, 1f)
                val layoutParams = GridLayout.LayoutParams(rowSpec, columnSpec)
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    layoutParams.height = 0
                    layoutParams.width = 0
                } else {
                    layoutParams.width = 0
                    layoutParams.height = DisplayUtil.dp2px(mContext!!, 64f)
                }
                layoutParams.setGravity(Gravity.FILL)
                courseInfoView.layoutParams = layoutParams
                courseInfoView.setOnClickListener { v: View? -> jumpToEditCourseInfo(null) }
                mTableLayout!!.addView(courseInfoView)
            }
        }
    }

    private fun genCourseItemView(courseInfo: CourseInfoBean): TextView {
        val courseInfoView = TextView(mContext)
        courseInfoView.gravity = Gravity.CENTER
        courseInfoView.text = String.format("%1s@%2s", courseInfo.name, courseInfo.classroom)
        courseInfoView.setTextColor(ContextCompat.getColor(mContext!!, R.color.white))
        courseInfoView.setBackgroundResource(R.drawable.course_bg_cyan)
        val rowSpec = GridLayout.spec(courseInfo.beginTime - 1, courseInfo.length, 1f)
        val columnSpec = GridLayout.spec(courseInfo.weekTime.toInt() - 1, 1, 1f)
        val layoutParams = GridLayout.LayoutParams(rowSpec, columnSpec)
        layoutParams.height = 0
        layoutParams.width = 0
        layoutParams.setGravity(Gravity.FILL)
        courseInfoView.layoutParams = layoutParams
        courseInfoView.setOnClickListener { jumpToEditCourseInfo(courseInfo) }
        courseInfoView.setOnLongClickListener { v: View ->
            showDeleteTipDialog(v, courseInfo)
            true
        }
        return courseInfoView
    }

    private fun jumpToEditCourseInfo(courseInfo: CourseInfoBean?) {
        val intent = Intent(this@MainActivity, EditCourseActivity::class.java)
        if (null != courseInfo) {
            Log.d(TAG, "jumpToEditCourseInfo: " + courseInfo.id)
            intent.putExtra("CourseId", courseInfo.id)
        }
        startActivity(intent)
    }

    private fun showDeleteTipDialog(view: View, courseInfoBean: CourseInfoBean) {
        if (null == mDialog) {
            val dialog = AlertDialog.Builder(mContext!!)
                    .setMessage("删除后，该课程将从课程表中移除")
                    .setPositiveButton("删除") { _: DialogInterface?, _: Int ->
                        mCourseViewModel!!.deleteCourseInfo(courseInfoBean)
                        mTableLayout!!.removeView(view)
                    }
                    .setNegativeButton("取消") { dialog12: DialogInterface, _: Int -> dialog12.dismiss() }
            mDialog = dialog.show()
        }
        mDialog!!.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCourseViewModel!!.getAllCourseInfo().removeObservers(this)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}