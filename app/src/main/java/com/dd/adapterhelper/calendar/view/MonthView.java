package com.dd.adapterhelper.calendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.dd.adapterhelper.calendar.listener.OnCalendarClickListener;
import com.dd.adapterhelper.calendar.model.CalendarModel;
import com.dd.adapterhelper.calendar.model.LunarCalendarModel;
import com.dd.adapterhelper.calendar.utils.CalendarUtil;
import com.dd.adapterhelper.calendar.utils.LunarCalendarUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by 郑冬冬 on 2017/12/7.
 */
public class MonthView extends View {

    private int NUM_COLUMNS = 7;
    private int NUM_ROWS = 7;

    // 在recyclerView中所在的位置
    private int mPosition;

    // 表示这个 View 显示哪个月的数据
    private int mCurrYear, mCurrMonth;
    private int mTodayYear, mTodayMonth, mTodayDay;
    // 选择的日期
    private List<CalendarModel> mSelectDays = new ArrayList<>();

    // 普通画笔
    private Paint mPaint;
    // 农历画笔
    private Paint mLunarPaint;
    // 已选日期的画笔
    private Paint mSelectPaint;

    // 普通日期颜色
    private int mDayTextColor = Color.parseColor("#FF209FE9");
    // 选中的日期圆圈的颜色
    private int mSelectCircleColor = Color.parseColor("#3D81E2");
    // 选中的字体颜色
    private int mSelectTextColor = Color.parseColor("#FFFFFF");
    // 今天的日期颜色
    private int mTodayTextColor = Color.parseColor("#FF0000");
    // 农历的日期颜色
    private int mLunarTextColor = Color.parseColor("#ACA9BC");
    // 节日的日期颜色
    private int mHolidayTextColor = Color.parseColor("#A68BFF");
    // 不可选的日期颜色
    private int mUnableSelectTextColor = Color.parseColor("#ACA9BC");
    // 当月包含的上个月/下个月的日期颜色
    private int mLastOrNextMonthTextColor = Color.parseColor("#ACA9BC");
    // 标题的日期颜色
    private int mTitleTextColor = Color.parseColor("#ACA9BC");

    // 单行/单列大小
    private int mColumnSize, mRowSize;
    // 选择的圆圈大小 -- 半径
    private int mSelectCircleSize;
    // 普通日期字体大小
    private int mDayTextSize = 16;
    // 农历字体大小
    private int mLunarTextSize = 10;
    // 标题字体大小
    private int mTitleTextSize = 18;

    // 设置可选的最大日期数量
    private boolean isShowTitle = true;
    private CalendarModel mUnableBeforeDay;
    private CalendarModel mUnableNextDay;

    // 用于计算单位转换
    private DisplayMetrics mDisplayMetrics;
    private GestureDetector mGestureDetector;

    private OnCalendarClickListener mListener;

    public MonthView(Context context) {
        this(context, null);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initData();
    }

    // 初始化一些数据
    private void initData() {
        Calendar today = Calendar.getInstance();
        mTodayYear = today.get(Calendar.YEAR);
        mTodayMonth = today.get(Calendar.MONTH);
        mTodayDay = today.get(Calendar.DAY_OF_MONTH);

        mDisplayMetrics = getResources().getDisplayMetrics();
        float scaledDensity = mDisplayMetrics.scaledDensity;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mDayTextSize * scaledDensity);

        mLunarPaint = new Paint();
        mLunarPaint.setAntiAlias(true);
        mLunarPaint.setTextSize(mLunarTextSize * scaledDensity);
        mLunarPaint.setColor(mLunarTextColor);

        mSelectPaint = new Paint();
        mSelectPaint.setAntiAlias(true);
        mSelectPaint.setStrokeWidth(mDisplayMetrics.density * 1 + 0.5f);
        mSelectPaint.setColor(mSelectCircleColor);

        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                doClickAction((int) e.getX(), (int) e.getY());
                return true;
            }
        });
    }

    /**
     * 先初始化数据, 最后再设置日期 因为只有这里调用了 invalidate()
     *
     * @param year
     * @param month
     */
    public void setDate(int year, int month, int position) {
        this.mCurrYear = year;
        this.mCurrMonth = month;
        this.mPosition = position;

        invalidate();
    }

    public void setOnCalendarClickListener(OnCalendarClickListener listener) {
        mListener = listener;
    }

    public CalendarModel getCurrentDays() {
        return new CalendarModel(mCurrYear, mCurrMonth, 1);
    }

    /**
     * 设置普通日期文字大小
     *
     * @param size
     */
    public void setDayTextSize(int size) {
        this.mDayTextSize = size;
        mPaint.setTextSize(mDisplayMetrics.scaledDensity * size);
    }

    /**
     * 设置普通日期文字颜色
     *
     * @param color
     */
    public void setDayTextColor(int color) {
        this.mDayTextColor = color;
    }

    /**
     * 设置上一月/下一月文字颜色
     *
     * @param color
     */
    public void setLastOrNextMonthTextColor(int color) {
        this.mLastOrNextMonthTextColor = color;
    }

    /**
     * 设置不可选文字颜色
     *
     * @param color
     */
    public void setUnableSelectTextColor(int color) {
        this.mUnableSelectTextColor = color;
    }

    /**
     * 设置已选择字体颜色
     *
     * @param color
     */
    public void setSelectTextColor(int color) {
        this.mSelectTextColor = color;
    }

    /**
     * 设置已选择圆圈的颜色
     *
     * @param color
     */
    public void setSelectCircleColor(int color) {
        this.mSelectCircleColor = color;
    }

    /**
     * 设置农历文字大小
     *
     * @param size
     */
    public void setLunarTextSize(int size) {
        this.mLunarTextSize = size;
        mLunarPaint.setTextSize(mDisplayMetrics.scaledDensity * size);
    }

    /**
     * 设置农历文字颜色
     *
     * @param color
     */
    public void setLunarTextColor(int color) {
        this.mLunarTextColor = color;
    }

    /**
     * 设置节日文字颜色
     *
     * @param color
     */
    public void setHolidayTextColor(int color) {
        this.mHolidayTextColor = color;
    }

    /**
     * 设置今天文字颜色
     *
     * @param color
     */
    public void setTodayTextColor(int color) {
        this.mTodayTextColor = color;
    }

    /**
     * 设置标题文字颜色
     *
     * @param color
     */
    public void setTitleTextColor(int color) {
        this.mTitleTextColor = color;
    }

    /**
     * 设置是否显示月份标题
     *
     * @param isShow
     */
    public void setShowTitle(boolean isShow) {
        this.isShowTitle = isShow;
        if (isShowTitle) {
            NUM_ROWS = 7;
        } else {
            NUM_ROWS = 6;
        }
    }

    /**
     * 设置标题文字大小
     *
     * @param size
     */
    public void setTitleTextSize(int size) {
        this.mTitleTextSize = size;
    }

    /**
     * 设置已选择的日期
     *
     * @param models
     */
    public void setSelectDay(List<CalendarModel> models) {
        mSelectDays = models == null ? new ArrayList<CalendarModel>() : models;
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
     * 计算宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = mDisplayMetrics.densityDpi * 200;
        }
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = mDisplayMetrics.densityDpi * 300;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private void doClickAction(int x, int y) {
        if (y > getHeight()) {
            return;
        }
        if (isShowTitle && y <= mRowSize) {
            return;
        }
        int row = y / mRowSize - (isShowTitle ? 1 : 0);
        int col = x / mColumnSize;
        col = Math.min(col, 6);
        int clickYear = mCurrYear, clickMonth = mCurrMonth, clickDay = 0;
        int weekNumber = CalendarUtil.getFirstDayWeek(mCurrYear, mCurrMonth);
        if (row == 0) {
            if (weekNumber == 1) {
                // 本月的点击
                clickDay = col + 1;
            } else {
                if (weekNumber <= col + 1) {
                    // 本月的点击
                    clickDay = col - weekNumber + 2;
                } else {
                    // 上个月的点击
                    if (clickMonth == 0) {
                        clickMonth = 11;
                        clickYear--;
                    } else {
                        clickMonth--;
                    }
                    int daysInClickMonths = CalendarUtil.getMonthDays(clickYear, clickMonth);
                    clickDay = daysInClickMonths - (weekNumber - (col + 1) - 1);
                    if (isUnableDay(clickYear, clickMonth, clickDay)) {
                        return;
                    }
                    if (mListener != null) {
                        mListener.onLastMonthClick(mPosition - 1, clickYear, clickMonth, clickDay);
                    }
                    log("click last month --  year : " + clickYear + " , month : " + clickMonth + " , day : " + clickDay);
                    return;
                }
            }
        } else {
            clickDay = (NUM_COLUMNS - weekNumber + 1) + (row - 1) * NUM_COLUMNS + (col + 1);
            int daysInClickMonths = CalendarUtil.getMonthDays(clickYear, clickMonth);
            if (clickDay > daysInClickMonths) {
                // 下个月的点击
                clickDay = clickDay - daysInClickMonths;
                clickMonth++;
                if (clickMonth > 11) {
                    clickMonth = 0;
                    clickYear++;
                }
                if (isUnableDay(clickYear, clickMonth, clickDay)) {
                    return;
                }
                if (mListener != null) {
                    mListener.onNextMonthClick(mPosition + 1, clickYear, clickMonth, clickDay);
                }
                log("click next month --  year : " + clickYear + " , month : " + clickMonth + " , day : " + clickDay);
                return;
            }
        }
        if (isUnableDay(clickYear, clickMonth, clickDay)) {
            return;
        }
        // 本月点击到这
        if (mListener != null) {
            mListener.onCurrentMonthClick(clickYear, clickMonth, clickDay);
        }
        log("click this month --  year : " + clickYear + " , month : " + clickMonth + " , day : " + clickDay);
    }

    /**
     * 绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long time = System.currentTimeMillis();
        calcSize();
        if (mCurrYear > 0) {
            if (isShowTitle) {
                drawTitle(canvas);
            }

            int yearNum, monthNum, dayNum;
            int weekNumber = CalendarUtil.getFirstDayWeek(mCurrYear, mCurrMonth);
            if (weekNumber == 1) {
                yearNum = mCurrYear;
                monthNum = mCurrMonth;
                dayNum = 1;
            } else {
                int monthDays;
                if (mCurrMonth == 0) {
                    yearNum = mCurrYear - 1;
                    monthNum = 11;
                    monthDays = CalendarUtil.getMonthDays(yearNum, monthNum);
                } else {
                    yearNum = mCurrYear;
                    monthNum = mCurrMonth - 1;
                    monthDays = CalendarUtil.getMonthDays(yearNum, monthNum);
                }
                dayNum = monthDays - weekNumber + 2;
            }
            // 注意 这里计算农历时的月份要+1, 默认从0开始的
            LunarCalendarModel lunar = LunarCalendarUtil.solarToLunar(new CalendarModel(yearNum, monthNum + 1, dayNum));
            int lunarDayNum = lunar.lunarDay;
            int leapMonth = LunarCalendarUtil.leapMonth(lunar.lunarYear);
            int lunarDaysInMonth = LunarCalendarUtil.daysInMonth(lunar.lunarYear, lunar.lunarMonth, lunar.isLeap);
            int daysInMonth = CalendarUtil.getMonthDays(yearNum, monthNum);

            boolean lastLineHasCurrentDays = false;
            for (int i = 0; i < 42; i++) {
                int col = i % 7;
                int row = i / 7 + (isShowTitle ? 1 : 0);
                if (dayNum > daysInMonth) {
                    dayNum = 1;
                    monthNum++;
                    if (monthNum > 11) {
                        monthNum = 0;
                        yearNum++;
                    }
                    daysInMonth = CalendarUtil.getMonthDays(yearNum, monthNum);
                }
                if (row + 1 == NUM_ROWS) {
                    // 最后一行
                    if (isNextMonth(yearNum, monthNum) && !lastLineHasCurrentDays) {
                        // 如果最后一行全是下个月的数据, 跳过
                        break;
                    } else {
                        lastLineHasCurrentDays = true;
                    }
                }
                if (lunarDayNum > lunarDaysInMonth) {
                    lunarDayNum = 1;
                    if (lunar.lunarMonth == 12) {
                        lunar.lunarMonth = 1;
                        lunar.lunarYear = lunar.lunarYear + 1;
                    }
                    if (lunar.lunarMonth == leapMonth) {
                        lunarDaysInMonth = LunarCalendarUtil.daysInMonth(lunar.lunarYear, lunar.lunarMonth, lunar.isLeap);
                    } else {
                        lunar.lunarMonth++;
                        lunarDaysInMonth = LunarCalendarUtil.daysInLunarMonth(lunar.lunarYear, lunar.lunarMonth);
                    }
                }
                mLunarPaint.setColor(mHolidayTextColor);

                log("year : " + yearNum + " , month : " + monthNum + " , day : " + dayNum + " , currentYear : " + mCurrYear + " , currentMonth : " + mCurrMonth);
                drawSelectDayCircle(canvas, yearNum, monthNum, dayNum, col, row);
                drawDayText(canvas, yearNum, monthNum, dayNum, col, row);
                drawLunarOrHolidayText(canvas, yearNum, monthNum, dayNum, lunar.lunarYear, lunar.lunarMonth, lunarDayNum, col, row);

                lunarDayNum++;
                dayNum++;
            }
        }
        log("----end draw : " + (System.currentTimeMillis() - time));
    }

    /**
     * 绘制月份标题
     *
     * @param canvas
     */
    private void drawTitle(Canvas canvas) {
        int month = mCurrMonth + 1;
        String title = mCurrYear + "年" + (month < 10 ? "0" + month : month) + "月";
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mTitleTextColor);
        paint.setTextSize(mTitleTextSize * mDisplayMetrics.scaledDensity);

        float titleLength = paint.measureText(title);

        float startX = (getWidth() - titleLength) / 2;
        float startY = (mRowSize - paint.ascent() - mPaint.descent()) / 2;
        canvas.drawText(title, startX, startY, paint);

    }

    /**
     * 绘制月份数字
     *
     * @param canvas
     * @param year
     * @param month
     * @param day
     * @param col
     * @param row
     */
    private void drawDayText(Canvas canvas,
                             int year, int month, int day,
                             int col, int row) {
        String dayStr = String.valueOf(day);
        int startX = (int) (mColumnSize * col + (mColumnSize - mPaint.measureText(dayStr)) / 2);
        int startY = (int) (mRowSize * row + mRowSize / 2.6f - (mPaint.ascent() + mPaint.descent()) / 2.6f);
        if (!isNotThisMonth(year, month) && isTheDaySelect(year, month, day)) {
            mPaint.setColor(mSelectTextColor);
        } else if (year == mTodayYear && month == mTodayMonth && day == mTodayDay) {
            mPaint.setColor(mTodayTextColor);
        } else if (isUnableDay(year, month, day)) {
            mPaint.setColor(mUnableSelectTextColor);
        } else if (isNotThisMonth(year, month)) {
            mPaint.setColor(mLastOrNextMonthTextColor);
        } else {
            mPaint.setColor(mDayTextColor);
        }
        canvas.drawText(dayStr, startX, startY, mPaint);
    }

    /**
     * 绘制底部农历/节日
     *
     * @param canvas
     * @param year
     * @param month
     * @param day
     * @param lunarYear
     * @param lunarMonth
     * @param lunarDay
     * @param col
     * @param row
     */
    private void drawLunarOrHolidayText(Canvas canvas,
                                        int year, int month, int day,
                                        int lunarYear, int lunarMonth, int lunarDay,
                                        int col, int row) {
        // 是否为农历节日
        String dayStr = LunarCalendarUtil.getLunarHoliday(lunarYear, lunarMonth, lunarDay);
        if ("".equals(dayStr)) {
            // 是否为公历节日
            dayStr = CalendarUtil.getHolidayFromSolar(year, month, day);
            if ("".equals(dayStr)) {
                // 输出农历日期
                dayStr = LunarCalendarUtil.getLunarDayString(lunarMonth, lunarDay);
                mLunarPaint.setColor(mLunarTextColor);
            }
        }
        if (!isNotThisMonth(year, month) && isTheDaySelect(year, month, day)) {
            mLunarPaint.setColor(mSelectTextColor);
        } else if (year == mTodayYear && month == mTodayMonth && day == mTodayDay) {
            mLunarPaint.setColor(mTodayTextColor);
        }

        int startX = (int) (mColumnSize * col + (mColumnSize - mLunarPaint.measureText(dayStr)) / 2);
        int startY = (int) (mRowSize * row + mRowSize * 0.72f - (mLunarPaint.ascent() + mLunarPaint.descent()) / 2);
        canvas.drawText(dayStr, startX, startY, mLunarPaint);
    }

    /**
     * 如果当日期已被选中, 绘制选中的圆圈
     *
     * @param canvas
     * @param year
     * @param month
     * @param day
     * @param col
     * @param row
     * @return
     */
    private void drawSelectDayCircle(Canvas canvas, int year, int month, int day, int col, int row) {
        if (!isNotThisMonth(year, month)) {
            boolean isDaySelected = isTheDaySelect(year, month, day);
            if (isDaySelected) {
                // 这一天已被选中
                int startRecX = mColumnSize * col;
                int startRecY = mRowSize * row;
                int endRecX = startRecX + mColumnSize;
                int endRecY = startRecY + mRowSize;
                canvas.drawCircle((startRecX + endRecX) / 2, (startRecY + endRecY) / 2, mSelectCircleSize, mSelectPaint);
            }
        }
    }

    /**
     * 判断是否是本月
     *
     * @param year
     * @param month
     * @return
     */
    private boolean isNotThisMonth(int year, int month) {
        return year != mCurrYear || month != mCurrMonth;
    }

    /**
     * 是否是下一个月
     *
     * @param year
     * @param month
     * @return
     */
    private boolean isNextMonth(int year, int month) {
        if (year > mCurrYear) {
            return true;
        }
        if (month > mCurrMonth) {
            return true;
        }
        return false;
    }

    /**
     * 当前日期是否被选中
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    private boolean isTheDaySelect(int year, int month, int day) {
        if (mSelectDays == null || mSelectDays.size() <= 0) {
            return false;
        }
        for (CalendarModel model : mSelectDays) {
            if (model.day == day && model.month == month && model.year == year) {
                return true;
            }
        }
        return false;
    }

    /**
     * 当前日期是否可用
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    private boolean isUnableDay(int year, int month, int day) {
        if (mUnableBeforeDay != null) {
            if (year < mUnableBeforeDay.year) {
                return true;
            } else if (year == mUnableBeforeDay.year) {
                if (month < mUnableBeforeDay.month) {
                    return true;
                } else if (month == mUnableBeforeDay.month) {
                    if (day < mUnableBeforeDay.day) {
                        return true;
                    }
                }
            }
        }
        if (mUnableNextDay != null) {
            if (year > mUnableNextDay.year) {
                return true;
            } else if (year == mUnableNextDay.year) {
                if (month > mUnableNextDay.month) {
                    return true;
                } else if (month == mUnableNextDay.month) {
                    if (day > mUnableNextDay.day) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void calcSize() {
        mColumnSize = getWidth() / NUM_COLUMNS;
        mRowSize = getHeight() / NUM_ROWS;
        mSelectCircleSize = (int) (mRowSize / 2);
    }

    private void log(String msg){
        //LogUtil.log(msg);
    }
}
