package com.dd.adapterhelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dd.adapterhelper.calendar.CalendarView;
import com.dd.adapterhelper.calendar.model.CalendarModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 郑冬冬 on 2017/12/9.
 */

public class CalendarActivity extends AppCompatActivity {

    private CalendarModel unableNextDay;
    private CalendarModel unableBeforeDay;

    private CalendarView recyclerView;

    private boolean isVertical = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        recyclerView = findViewById(R.id.recycler);

        Calendar calendar = Calendar.getInstance();

        List<CalendarModel> list = new ArrayList<>();

        unableBeforeDay = new CalendarModel(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 3);
        list.add(new CalendarModel(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1));
        calendar.add(Calendar.MONTH, 1);
        list.add(new CalendarModel(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1));
        calendar.add(Calendar.MONTH, 1);
        list.add(new CalendarModel(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1));
        calendar.add(Calendar.MONTH, 1);
        list.add(new CalendarModel(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1));
        calendar.add(Calendar.MONTH, 1);
        list.add(new CalendarModel(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1));
        calendar.add(Calendar.MONTH, 1);
        list.add(new CalendarModel(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1));
        calendar.add(Calendar.MONTH, 1);
        unableNextDay = new CalendarModel(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 15);
        list.add(new CalendarModel(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1));
        calendar.add(Calendar.MONTH, 1);

        recyclerView.setMaxSelectNum(3);
        recyclerView.setHorizontalScroll(isVertical);
        recyclerView.setUnableBeforeDay(unableBeforeDay);
        recyclerView.setUnableNextDay(unableNextDay);
        recyclerView.init(list);

    }

    public void switchOrientation(View view) {
        isVertical = !isVertical;
        recyclerView.setHorizontalScroll(isVertical);
    }

}
