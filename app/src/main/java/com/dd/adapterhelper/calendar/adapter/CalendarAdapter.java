package com.dd.adapterhelper.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dd.adapterhelper.calendar.listener.OnCalendarClickListener;
import com.dd.adapterhelper.calendar.model.CalendarModel;
import com.dd.adapterhelper.calendar.view.MonthView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郑冬冬 on 2017/12/9.
 */

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MonthViewHolder> {

    private List<CalendarModel> mList = new ArrayList<>();
    private List<CalendarModel> mSelectList = new ArrayList<>();

    private CalendarModel mUnableNextDay;
    private CalendarModel mUnableBeforeDay;

    private int mHeight;
    private int mWidth;
    private OnCalendarClickListener mListener;

    public CalendarAdapter(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    @Override
    public MonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MonthView view = new MonthView(parent.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(mWidth, mHeight));
        return new MonthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MonthViewHolder holder, int position) {
        MonthView monthView = (MonthView) holder.itemView;
        monthView.setSelectDay(mSelectList);
        monthView.setUnableBeforeDay(mUnableBeforeDay);
        monthView.setUnableNextDay(mUnableNextDay);
        monthView.setShowTitle(true);
        monthView.setOnCalendarClickListener(mListener);
        monthView.setDate(mList.get(holder.getLayoutPosition()).year, mList.get(holder.getLayoutPosition()).month, holder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 初始化
     *
     * @param list
     */
    public void setNewData(List<CalendarModel> list) {
        this.mList = list == null ? new ArrayList<CalendarModel>() : list;
        notifyDataSetChanged();
    }

    /**
     * 设置点击监听
     *
     * @param listener
     */
    public void setOnCalendarClickListener(OnCalendarClickListener listener) {
        this.mListener = listener;
    }

    /**
     * 设置之前的日期不可选
     *
     * @param unableBeforeDay
     */
    public void setUnableBeforeDay(CalendarModel unableBeforeDay) {
        this.mUnableBeforeDay = unableBeforeDay;
    }

    /**
     * 设置之后的日期不可选
     *
     * @param unableNextDay
     */
    public void setUnableNextDay(CalendarModel unableNextDay) {
        this.mUnableNextDay = unableNextDay;
    }

    /**
     * 设置已选择的日期
     *
     * @param models
     */
    public void setSelectDay(List<CalendarModel> models) {
        mSelectList = models == null ? new ArrayList<CalendarModel>() : models;
    }


    public class MonthViewHolder extends RecyclerView.ViewHolder {

        public MonthViewHolder(View itemView) {
            super(itemView);
        }
    }
}
