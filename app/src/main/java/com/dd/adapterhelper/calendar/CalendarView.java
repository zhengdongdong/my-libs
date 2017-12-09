package com.dd.adapterhelper.calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.Toast;

import com.dd.adapterhelper.calendar.adapter.CalendarAdapter;
import com.dd.adapterhelper.calendar.listener.OnCalendarClickListener;
import com.dd.adapterhelper.calendar.listener.OnDaySelectedListener;
import com.dd.adapterhelper.calendar.model.CalendarModel;
import com.dd.adapterhelper.calendar.utils.ViewPagerSnapHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 郑冬冬 on 2017/12/9.
 */

public class CalendarView extends RecyclerView implements OnCalendarClickListener {

    private List<CalendarModel> mSelectDays = new ArrayList<>();

    private CalendarAdapter mAdapter;

    private int mMaxSelectNum = 1;

    private OnDaySelectedListener mDaySelectListener;
    private ViewPagerSnapHelper helper;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


        WindowManager wm = ((Activity) context).getWindowManager();

        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        int width = point.x;
        int height = point.x / 7 * 5;

        mAdapter = new CalendarAdapter(width, height);
        mAdapter.setOnCalendarClickListener(this);
        mAdapter.setSelectDay(mSelectDays);

        setLayoutManager(new LinearLayoutManager(context));
        setAdapter(mAdapter);
    }

    public void init(List<CalendarModel> list) {
        mAdapter.setNewData(list);
    }

    @Override
    public void onLastMonthClick(int position, int year, int month, int day) {
        dealClick(year, month, day);
    }

    @Override
    public void onNextMonthClick(int position, int year, int month, int day) {
        dealClick(year, month, day);
    }

    @Override
    public void onCurrentMonthClick(int year, int month, int day) {
        dealClick(year, month, day);
    }

    private void dealClick(int year, int month, int day) {
        boolean notDoNext = resetSelectList(year, month, day);
        if (!notDoNext) {
            refreshViews();
        } else {
            toast("最多选择" + mMaxSelectNum + "天");
        }
    }

    /**
     * 重置已选
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    private boolean resetSelectList(int year, int month, int day) {
        if (mMaxSelectNum == 1) {
            if (mSelectDays.size() > 0) {
                mSelectDays.clear();
            }
            mSelectDays.add(new CalendarModel(year, month, day));
        } else {
            int removePosition = -1;
            CalendarModel model = new CalendarModel(year, month, day);
            for (int i = 0; i < mSelectDays.size(); i++) {
                if (mSelectDays.get(i).equals(model)) {
                    removePosition = i;
                    break;
                }
            }
            if (mSelectDays.size() >= mMaxSelectNum && removePosition < 0) {
                return true;
            }
            if (removePosition >= 0) {
                mSelectDays.remove(removePosition);
            } else {
                mSelectDays.add(model);
            }
        }
        return false;
    }

    /**
     * 刷新view
     */
    private void refreshViews() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).invalidate();
        }
        if (mDaySelectListener != null) {
            mDaySelectListener.onDaySelected(mSelectDays);
        }
    }

    /**
     * 设置已选择的日期
     *
     * @param models
     */
    public void setSelectDay(List<CalendarModel> models) {
        mSelectDays = models == null ? new ArrayList<CalendarModel>() : models;
        mAdapter.setSelectDay(mSelectDays);
    }

    /**
     * 设置之前的日期不可选
     *
     * @param unableBeforeDay
     */
    public void setUnableBeforeDay(CalendarModel unableBeforeDay) {
        mAdapter.setUnableBeforeDay(unableBeforeDay);
    }

    /**
     * 设置之后的日期不可选
     *
     * @param unableNextDay
     */
    public void setUnableNextDay(CalendarModel unableNextDay) {
        mAdapter.setUnableNextDay(unableNextDay);
    }

    public void setMaxSelectNum(int num) {
        this.mMaxSelectNum = num;
    }

    public void setHorizontalScroll(boolean horizontal) {
        if (horizontal) {
            setLayoutManager(new LinearLayoutManager(getContext(), HORIZONTAL, false));
            if (helper == null) {
                helper = new ViewPagerSnapHelper();
            }
            helper.attachToRecyclerView(this);
        } else {
            setLayoutManager(new LinearLayoutManager(getContext()));
            if (helper != null) {
                helper.attachToRecyclerView(null);
            }
        }

    }

    public void setDaySelectListener(OnDaySelectedListener listener) {
        this.mDaySelectListener = listener;
    }

    private void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
