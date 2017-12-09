package com.dd.adapterhelper.calendar.listener;

/**
 * Created by 郑冬冬 on 2017/12/9.
 */

public interface OnCalendarClickListener {
    void onLastMonthClick(int position, int year, int month, int day);

    void onNextMonthClick(int position, int year, int month, int day);

    void onCurrentMonthClick(int year, int month, int day);
}
