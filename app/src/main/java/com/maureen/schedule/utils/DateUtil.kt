package com.maureen.schedule.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Function: 时间日期工具类
 * Create:  2017/11/25
 *
 * @author lianml
 */
fun Long.convertToDate(pattern: String): String {
    return SimpleDateFormat(pattern, Locale.CHINA).format(this)
}

/**
 * 获取当前日期
 *
 * @return X月X日
 */
val currentDate: String
    get() {
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("GMT+8:00")
        // 获取当前月份
        val mMonth = (calendar[Calendar.MONTH] + 1).toString()
        // 获取当前月份的日期
        val mDay = calendar[Calendar.DAY_OF_MONTH].toString()
        return mMonth + "月" + mDay + "日"
    }

fun Long.convertToYearDateStr(): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar.timeZone = TimeZone.getTimeZone("GMT+8:00")
    val year = calendar[Calendar.YEAR].toString()
    // 获取当前月份
    val month = (calendar[Calendar.MONTH] + 1).toString()
    // 获取当前月份的日期
    val day = calendar[Calendar.DAY_OF_MONTH].toString()
    return "${year}年${month}月${day}日"
}

/**
 * 获取当前周的所有日期
 *
 * @return
 */
fun getDayOfCurrentWeek(weekLength: Int): List<WeekDay> {
    // 获取本周的第一天，从周一开始
    val calendar = Calendar.getInstance(Locale.FRANCE)
    val firstDayOfWeek = calendar.firstDayOfWeek
    val curDay = calendar[Calendar.DAY_OF_MONTH]
    val list: MutableList<WeekDay> = ArrayList()
    for (i in 0 until weekLength) {
        calendar[Calendar.DAY_OF_WEEK] = firstDayOfWeek + i
        val weekDay = WeekDay()
        val date = SimpleDateFormat("dd", Locale.CHINA).format(calendar.time)
        weekDay.date = date
        weekDay.isCurrent = date.toInt() == curDay
        weekDay.weekIndex = SimpleDateFormat("E", Locale.CHINA).format(calendar.time)
        list.add(weekDay)
    }
    return list
}

class WeekDay {
    var weekIndex: String? = null
    var date: String? = null
    var isCurrent = false
}