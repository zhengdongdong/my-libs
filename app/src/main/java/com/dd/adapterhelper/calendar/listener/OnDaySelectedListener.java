package com.dd.adapterhelper.calendar.listener;

import com.dd.adapterhelper.calendar.model.CalendarModel;

import java.util.List;

/**
 * Created by 郑冬冬 on 2017/12/9.
 */

public interface OnDaySelectedListener {
    void onDaySelected(List<CalendarModel> days);
}
