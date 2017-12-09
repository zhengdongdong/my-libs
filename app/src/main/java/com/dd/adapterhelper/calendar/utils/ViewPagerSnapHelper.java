package com.dd.adapterhelper.calendar.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 郑冬冬 on 2017/12/9.
 */

public class ViewPagerSnapHelper extends PagerSnapHelper {

    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        return super.calculateDistanceToFinalSnap(layoutManager, targetView);
    }

    @Override
    public boolean onFling(int velocityX, int velocityY) {
        return super.onFling(velocityX, velocityY);
    }

    @Nullable
    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        return super.findSnapView(layoutManager);
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        return super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
    }

    @Override
    protected LinearSmoothScroller createSnapScroller(RecyclerView.LayoutManager layoutManager) {
        return super.createSnapScroller(layoutManager);
    }

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
        super.attachToRecyclerView(recyclerView);
    }

    @Override
    public int[] calculateScrollDistance(int velocityX, int velocityY) {
        return super.calculateScrollDistance(velocityX, velocityY);
    }

    @Nullable
    @Override
    protected RecyclerView.SmoothScroller createScroller(RecyclerView.LayoutManager layoutManager) {
        return super.createScroller(layoutManager);
    }
}
