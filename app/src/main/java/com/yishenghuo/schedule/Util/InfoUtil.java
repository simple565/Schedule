package com.yishenghuo.schedule.Util;

/**
 * Created by ihome on 2017/11/19.
 * 工具类
 * 解析课程数据
 * 获取当前时间
 */

public class InfoUtil {

    public static int getLength(int beginTime, int endTime) {
        return endTime - beginTime + 1;
    }

    public static int getBeginTime(String string, int beginTime) {
        if (string.equals("下午"))
            beginTime = beginTime + 4;
        else if (string.equals("晚上"))
            beginTime = beginTime + 8;
        return beginTime;
    }

    public static int getEndTime(String string, int endTime) {
        if (string.equals("下午"))
            endTime = endTime + 4;
        else if (string.equals("晚上"))
            endTime = endTime + 8;
        return endTime;
    }

    public static String isLegal(int beginTime, int endTime, int beginWeek, int endWeek) {
        String tip = " ";
        if (beginTime > endTime)
            tip = "上课时间设置错误";
        if (beginWeek > endWeek)
            tip = "上课周数设置错误";
        return tip;
    }

    public static boolean isOnWeek(int beginWeek, int endWeek, int nowWeek, int weekType) {
        boolean result = false;
        if (nowWeek < endWeek && nowWeek > beginWeek) {
            if (weekType == 1 || nowWeek % 2 != 0)
                result = true;
            else if (weekType == 2 || nowWeek % 2 == 0)
                result = true;
        } else if (nowWeek == beginWeek && nowWeek == endWeek) {
            if (weekType == 1 || nowWeek % 2 != 0)
                result = true;
            else if (weekType == 2 || nowWeek % 2 == 0)
                result = true;
        }
        return result;
    }

    public static int getNowWeek(String string) {
        int weekday = 1;
        if (string.contains("一"))
            weekday = 1;
        if (string.contains("二"))
            weekday = 2;
        if (string.contains("三"))
            weekday = 3;
        if (string.contains("四"))
            weekday = 4;
        if (string.contains("五"))
            weekday = 5;
        if (string.contains("六"))
            weekday = 6;
        if (string.contains("日"))
            weekday = 7;
        return weekday;
    }


}

