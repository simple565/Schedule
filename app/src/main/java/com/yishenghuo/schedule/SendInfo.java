package com.yishenghuo.schedule;

/**
 * Created by ihome on 2017/11/16.
 */

public interface SendInfo {
    /**
     *
     * @param name 课程名称
     * @param classroom 教室
     * @param teacher 教师
     * @param weekTime 周几
     * @param period 时间段  上午 下午 晚上
     * @param weekType 单双周
     * @param beginWeek 起始周
     * @param endWeek 结束周
     * @param beginTime 上课节数
     * @param endTime 下课节数
     */
    void onSendDialogInfo(String name, String classroom, String teacher, String weekTime,String period,
                          int weekType, int beginWeek, int endWeek, int beginTime, int endTime);
}
