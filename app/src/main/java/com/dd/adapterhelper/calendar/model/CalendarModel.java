package com.dd.adapterhelper.calendar.model;

/**
 * Created by 郑冬冬 on 2017/12/7.
 */

public class CalendarModel {
    public int year;
    public int month;
    public int day;

    public CalendarModel() {

    }

    public CalendarModel(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CalendarModel)) {
            return false;
        }
        CalendarModel model = (CalendarModel) obj;
        return this.year == model.year
                && this.month == model.month
                && this.day == model.day;
    }
}
