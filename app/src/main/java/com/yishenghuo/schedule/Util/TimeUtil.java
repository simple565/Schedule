package com.yishenghuo.schedule.Util;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by ihome on 2017/11/25.
 */

public class TimeUtil {

    public static String getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        return mMonth + "月" + mDay + "日";
    }

}
