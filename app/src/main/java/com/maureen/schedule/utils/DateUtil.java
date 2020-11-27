package com.maureen.schedule.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.NonNull;

/**
 * Function: 时间日期工具类
 * Create:  2017/11/25
 *
 * @author lianml
 */

public class DateUtil {

    /**
     * 获取当前月份
     *
     * @return X月
     */
    public static String getCurrentMonth() {
        return new SimpleDateFormat("MM", Locale.CHINA).format(System.currentTimeMillis()) + "\n月";
    }


    /**
     * 获取当前日期
     *
     * @return X月X日
     */
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        // 获取当前月份
        String mMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        // 获取当前月份的日期
        String mDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        return mMonth + "月" + mDay + "日";
    }

    /**
     * 获取当前周的所有日期
     *
     * @return
     */
    public static List<WeekDay> getDayOfCurrentWeek() {
        int weekLength = 7;
        // 获取本周的第一天，从周一开始
        Calendar calendar = Calendar.getInstance(Locale.FRANCE);
        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        List<WeekDay> list = new ArrayList<>();
        for (int i = 0; i < weekLength; i++) {
            calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + i);
            WeekDay weekDay = new WeekDay();
            String date = new SimpleDateFormat("dd", Locale.CHINA).format(calendar.getTime());
            weekDay.setDate(date);
            weekDay.setCurrent(Integer.parseInt(date) == curDay);
            weekDay.setWeekIndex(new SimpleDateFormat("E", Locale.CHINA).format(calendar.getTime()));
            System.out.println(weekDay.toString());
            list.add(weekDay);
        }

        return list;
    }

    public static class WeekDay {
        private String weekIndex;
        private String date;
        private boolean isCurrent = false;

        public String getWeekIndex() {
            return weekIndex;
        }

        public void setWeekIndex(String weekIndex) {
            this.weekIndex = weekIndex;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public boolean isCurrent() {
            return isCurrent;
        }

        public void setCurrent(boolean current) {
            isCurrent = current;
        }

        @NonNull
        @Override
        public String toString() {
            return date + "  " + weekIndex;
        }
    }

}
