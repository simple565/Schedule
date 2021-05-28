package com.maureen.schedule.view

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.widget.GridLayout
import android.widget.TextView
import com.maureen.schedule.R
import com.maureen.schedule.utils.DisplayUtil
import java.util.*

/**
 * Function:
 * Create:   2020/12/23
 * @author lianml
 */
class TableView(context: Context, rowCount: Int, columnCount: Int) : GridLayout(context) {
    private var mColumnCount: Int = columnCount
    private var mRowCount: Int = rowCount
    private var mCourseItemViews: HashMap<Int, TextView>? = null

    private fun initTable() {
        for (i in 0 until mRowCount) {
            for (j in 0 until mColumnCount) {
                val columnSpec = spec(i, 1f)
                val rowSpec = spec(j, 1f)
                val layoutParams = LayoutParams(rowSpec, columnSpec).apply {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        height = 0
                        width = 0
                    } else {
                        width = 0
                        height = DisplayUtil.dp2px(context, 64f)
                    }
                    setGravity(Gravity.FILL)
                }
                val courseInfoView = TextView(context).apply {
                    setBackgroundResource(R.color.white)
                    this.layoutParams = layoutParams
                    //setOnClickListener { jumpToEditCourseInfo(null) }
                }
                addView(courseInfoView)
            }
        }
    }


    private fun updateTable() {

    }
}