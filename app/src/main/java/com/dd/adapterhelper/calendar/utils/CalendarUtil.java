package com.dd.adapterhelper.calendar.utils;

import java.util.Calendar;

/**
 * Created by 郑冬冬 on 2017/12/7.
 */

public class CalendarUtil {
    /**
     * 通过年份和月份 得到当月的日子
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDays(int year, int month) {
        month++;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return -1;
        }
    }

    /**
     * 返回当前月份1号位于周几
     *
     * @param year  年份
     * @param month 月份，传入系统获取的，不需要正常的
     * @return 日：1		一：2		二：3		三：4		四：5		五：6		六：7
     */
    public static int getFirstDayWeek(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 根据国历获取假期
     *
     * @return
     */
    public static String getHolidayFromSolar(int year, int month, int day) {
        String message = "";
        if (month == 0 && day == 1) {
            message = "元旦";
        } else if (month == 1 && day == 14) {
            message = "情人节";
        } else if (month == 2 && day == 8) {
            message = "妇女节";
        } else if (month == 2 && day == 12) {
            message = "植树节";
        } else if (month == 3) {
            if (day == 1) {
                message = "愚人节";
            } else if (day >= 4 && day <= 6) {
                if (year <= 1999) {
                    int compare = (int) (((year - 1900) * 0.2422 + 5.59) - ((year - 1900) / 4));
                    if (compare == day) {
                        message = "清明节";
                    }
                } else {
                    int compare = (int) (((year - 2000) * 0.2422 + 4.81) - ((year - 2000) / 4));
                    if (compare == day) {
                        message = "清明节";
                    }
                }
            }
        } else if (month == 4 && day == 1) {
            message = "劳动节";
        } else if (month == 4 && day == 4) {
            message = "青年节";
        } else if (month == 5 && day == 1) {
            message = "儿童节";
        } else if (month == 6 && day == 1) {
            message = "建党节";
        } else if (month == 7 && day == 1) {
            message = "建军节";
        } else if (month == 8 && day == 10) {
            message = "教师节";
        } else if (month == 9 && day == 1) {
            message = "国庆节";
        } else if (month == 10 && day == 11) {
            message = "光棍节";
        } else if (month == 11 && day == 25) {
            message = "圣诞节";
        }
        return message;
    }


}
